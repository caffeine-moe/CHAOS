package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageCreateEvent

class RandomChoice : Command(arrayOf("randomchoice", "rchoice", "rch"),
    CommandInfo("Random Choice", "rch <Choices>", "Picks a random option/choice out of a list you provide.")) {
    override suspend fun onCalled(
        client : Client,
        event : MessageCreateEvent,
        args : MutableList<String>,
        cmd : String,
    ) = coroutineScope {
/*        val argj = args.joinToString(" ")
        val regex = "[0-9:]+".toRegex()
        val argspl = argj.split(regex)
        val err = when {
            argspl.isEmpty() -> "No options provided."
            argspl.size < 2 -> "Too few options."
            else -> ""
        }
        if (err.isNotBlank()) {
            event.channel.sendMessage(error(
                client,
                event,
                err,
                commandInfo
            )).thenAccept {
                launch {
                    onComplete(it, client, true)
                }
            }
            return@coroutineScope
        }

        event.channel.sendMessage(MessageBuilder()
            .appendLine(argspl.random().trim())
            .build()
        ).thenAccept {
            launch {
                onComplete(it, client, client.config.auto_delete.bot.content_generation)
            }
        }*/
    }
}