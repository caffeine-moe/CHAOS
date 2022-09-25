package org.caffeine.chaos.commands

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvents
import org.caffeine.chaos.api.utils.MessageBuilder
import org.caffeine.chaos.spamCock

class Spam : Command(arrayOf("spam"), CommandInfo("Spam", "spam <Message> <Amount>", "Spams messages in a channel.")) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvents.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) =
        coroutineScope {
            spamCock = false
            if (args.isEmpty()) {
                event.message.channel.sendMessage(error(client, event, "Not enough parameters.", commandInfo))
                    .await().also { message -> onComplete(message, client, true) }
                return@coroutineScope
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
                        .await().also { message -> onComplete(message, client, true) }
                    return@coroutineScope
                }
                var done = 0
                while (done < number && !spamCock) {
                    if (done % 10 == 0 && done != 0) {
                        withContext(Dispatchers.IO) {
                            Thread.sleep(5000)
                        }
                    }
                    event.message.channel.sendMessage(MessageBuilder().appendLine(msg).build())
                    done++
                    withContext(Dispatchers.IO) {
                        Thread.sleep(250)
                    }
                }
                if (done > 1) {
                    event.message.channel.sendMessage(
                        MessageBuilder().appendLine("Done spamming '$msg' $done times.")
                            .build()
                    )
                        .await().also { message -> onComplete(message, client, true) }
                }
                if (done == 1) {
                    event.message.channel.sendMessage(MessageBuilder().appendLine("Done spamming '$msg' once.").build())
                        .await().also { message -> onComplete(message, client, true) }
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
                            .await().also { message -> onComplete(message, client, true) }
                    }

                    is IndexOutOfBoundsException -> {
                        event.message.channel.sendMessage(error(client, event, "Not enough parameters.", commandInfo))
                            .await().also { message -> onComplete(message, client, true) }
                    }
                }
            }
        }
}