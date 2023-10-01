package org.caffeine.chaos.commands

import kotlinx.coroutines.delay
import org.caffeine.chaos.handlers.spamCock
import org.caffeine.octane.client.Client
import org.caffeine.octane.client.ClientEvent
import org.caffeine.octane.utils.awaitThen

class Spam : Command(arrayOf("spam"), CommandInfo("Spam", "spam <Message> <Amount>", "Spams messages in a channel.")) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : List<String>,
        cmd : String,
    ) {
        spamCock = false
        if (args.isEmpty()) {
            event.message.channel.sendMessage(error(client, event, "Not enough parameters.", commandInfo))
                .awaitThen { message -> onComplete(message, false) }
            return
        }
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
                    .awaitThen { message -> onComplete(message, false) }
                return
            }
            val msg = args.joinToString(" ").removeSuffix(number.toString()).trim()
            var done = 0
            for (i in 1..number) {
                if (done % 10 == 0 && done != 0) delay(5000)
                event.channel.sendMessage(msg).await()
                done++
                if (spamCock) break
                delay(250)
            }
            event.message.channel.sendMessage("Done spamming '$msg' ${if (done > 1) "$done times" else "once"}")
                .awaitThen { message -> onComplete(message, false) }
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
                        .awaitThen { message -> onComplete(message, false) }
                }

                is IndexOutOfBoundsException -> {
                    event.message.channel.sendMessage(error(client, event, "Not enough parameters.", commandInfo))
                        .awaitThen { message -> onComplete(message, false) }
                }
            }
        }
    }
}