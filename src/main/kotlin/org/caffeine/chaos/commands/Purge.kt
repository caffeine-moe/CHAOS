package org.caffeine.chaos.commands

import kotlinx.coroutines.delay
import org.caffeine.chaos.handlers.purgeCock
import org.caffeine.octane.client.Client
import org.caffeine.octane.client.ClientEvent
import org.caffeine.octane.entities.asSnowflake
import org.caffeine.octane.entities.channels.TextCapableChannel
import org.caffeine.octane.entities.message.MessageFilters
import org.caffeine.octane.typedefs.MessageType
import org.caffeine.octane.utils.awaitThen

class Purge : Command(
    arrayOf("purge", "sclear"),
    CommandInfo("Purge", "purge [Channel] <Amount>", "Deletes a specified amount of YOUR messages from a channel.")
) {

    private suspend fun resolveChannel(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : List<String>,
    ) : TextCapableChannel? {
        return when {
            args.isEmpty() -> {
                event.channel.sendMessage(error(client, event, "Not enough parameters.", commandInfo))
                    .awaitThen { message -> onComplete(message, false) }
                null
            }

            args.size == 1 -> {
                event.channel
            }

            args.size == 2 -> {
                val channel = client.user.textChannels[args[1].asSnowflake()]
                channel
            }

            else -> {
                event.channel.sendMessage(error(client, event, "Too many arguments.", commandInfo))
                    .awaitThen { message -> onComplete(message, false) }
                null
            }
        }
    }

    private suspend fun resolveNum(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : List<String>,
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
                    .awaitThen { message -> onComplete(message, false) }
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
                        .awaitThen { message -> onComplete(message, false) }
                    return null
                }
            }
        }
    }

    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : List<String>,
        cmd : String,
    ) {
        purgeCock = false
        val channel = resolveChannel(client, event, args) ?: return
        val num = resolveNum(client, event, args) ?: return
        var done = 0
        val messages = channel.fetchHistory(MessageFilters(needed = num))
        if (messages.isEmpty()) {
            event.channel.sendMessage("There is nothing to delete!").awaitThen { message -> onComplete(message, false) }
            return
        }
        messages
            .filter { message ->
                message.type == MessageType.DEFAULT
                        || message.type == MessageType.REPLY
            }
            .forEach { message ->
                if (done % 10 == 0 && done != 0) delay(5000)
                if (message.delete()) done++
                if (purgeCock) return
                delay(500)
            }
        event.channel.sendMessage("Removed $done message${if (done > 1 || done == 0) "s" else ""}!")
            .awaitThen { message -> onComplete(message, false) }
    }
}