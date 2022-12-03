package org.caffeine.chaos.commands

import arrow.core.getOrHandle
import kotlinx.coroutines.delay
import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.Snowflake
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.entities.channels.TextBasedChannel
import org.caffeine.chaos.api.entities.message.MessageFilters
import org.caffeine.chaos.api.typedefs.MessageType
import org.caffeine.chaos.purgeCock

class Purge : Command(
    arrayOf("purge", "sclear"),
    CommandInfo("Purge", "purge [Channel] <Amount>", "Deletes a specified amount of YOUR messages from a channel.")
) {

    suspend fun resolveChannel(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : MutableList<String>,
    ) : TextBasedChannel? {
        return when {
            args.size < 1 -> {
                event.channel.sendMessage(error(client, event, "Not enough parameters.", commandInfo))
                    .await().also { message -> onComplete(message, true) }
                null
            }

            args.size == 1 -> {
                event.channel
            }

            args.size == 2 -> {
                val channel = client.user.textChannels[Snowflake(args[1])]
                channel
            }

            else -> {
                event.channel.sendMessage(error(client, event, "Too many arguments.", commandInfo))
                    .await().also { message -> onComplete(message, true) }
                null
            }
        }
    }

    suspend fun resolveNum(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : MutableList<String>,
    ) : Int? {
        return if (!args.last().toString().contains("[^0-9]".toRegex())) {
            if (args.last().toInt() <= 0) {
                event.channel.sendMessage(
                    error(
                        client,
                        event,
                        "Amount must be higher than 0.",
                        commandInfo
                    )
                )
                    .await().also { message -> onComplete(message, true) }
                return null
            }
            args.last().toInt()
        } else {
            when (args.last()) {
                "max" -> {
                    0
                }

                "all" -> {
                    0
                }

                else -> {
                    event.message.channel.sendMessage(
                        error(
                            client,
                            event,
                            "${args.last()} is not an integer.",
                            commandInfo
                        )
                    )
                        .await().also { message -> onComplete(message, true) }
                    return null
                }
            }
        }
    }

    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) {
        purgeCock = false
        val channel = resolveChannel(client, event, args) ?: return
        val num = resolveNum(client, event, args) ?: return
        var done = 0
        val progress = event.channel.sendMessage("Fetching messages...").await()
        val messages = channel.fetchHistory(MessageFilters(authorId = client.user.id, needed = num))
        if (messages.isEmpty()) {
            progress.edit("There is nothing to delete!").await().also { message -> onComplete(message, true) }
            return
        }
        messages
            .filter { message ->
                message.author.id == client.user.id
                        && message.type == MessageType.DEFAULT
                        || message.type == MessageType.REPLY
                        && message.id != progress.id
            }
            .forEach { message ->
                if (done % 10 == 0 && done != 0) delay(5000)
                message.delete()
                done++
                progress.edit("Deleted message $done/${messages.size}...").await()
                if (purgeCock) return
                delay(1000)
            }
        progress.edit("Removed $done message${if (done > 1) "s" else ""}!")
            .await().also { message -> onComplete(message, true) }
    }
}