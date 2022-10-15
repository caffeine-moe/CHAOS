package org.caffeine.chaos.commands

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvents
import org.caffeine.chaos.api.typedefs.MessageOptions
import org.caffeine.chaos.api.utils.MessageBuilder
import org.caffeine.chaos.spamCock

class Spam : Command(arrayOf("spam"), CommandInfo("Spam", "spam <Message> <Amount>", "Spams messages in a channel.")) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvents.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) {
        spamCock = false
        if (args.isEmpty()) {
            event.message.channel.sendMessage(error(client, event, "Not enough parameters.", commandInfo))
                .await().also { message -> onComplete(message, true) }
            return
        }
        val msg = args.dropLast(1).joinToString(" ")
        try {
            val number = args.last().toLong()
            if (number <= 0) {
                event.message.channel.sendMessage(
                    error(
                        client,
                        event,
                        "Amount must be higher than 0.",
                        commandInfo
                    )
                )
                    .await().also { message -> onComplete(message, true) }
                return
            }
            var done = 0
            val message = MessageOptions(msg)
            for (i in 1..number) {
                if (done % 10 == 0 && done != 0) delay(5000)
                event.channel.sendMessage(message).await()
                done++
                if (spamCock) break
                delay(250)
            }
            if (done > 1) {
                event.message.channel.sendMessage(
                    MessageBuilder().appendLine("Done spamming '$msg' $done times.")
                        .build()
                )
                    .await().also { message -> onComplete(message, true) }
            }
            if (done == 1) {
                event.message.channel.sendMessage(MessageBuilder().appendLine("Done spamming '$msg' once.").build())
                    .await().also { message -> onComplete(message, true) }
            }
        } catch (e : Exception) {
            when (e) {
                is NumberFormatException -> {
                    event.message.channel.sendMessage(
                        error(
                            client,
                            event,
                            "'${args.last()}' is not an integer.",
                            commandInfo
                        )
                    )
                        .await().also { message -> onComplete(message, true) }
                }

                is IndexOutOfBoundsException -> {
                    event.message.channel.sendMessage(error(client, event, "Not enough parameters.", commandInfo))
                        .await().also { message -> onComplete(message, true) }
                }
            }
        }
    }
}
