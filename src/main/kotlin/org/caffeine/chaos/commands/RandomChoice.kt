package org.caffeine.chaos.commands

import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.utils.MessageBuilder
import org.caffeine.chaos.config

class RandomChoice : Command(
    arrayOf("randomchoice", "rchoice", "rch"),
    CommandInfo("Random Choice", "rch <Choices>", "Picks a random option/choice out of a list you provide.")
) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) {
        val argj = args.joinToString(" ")
        val argspl = argj.split(",")
        val err = when {
            argspl.isEmpty() -> "No options provided."
            argspl.size < 2 -> "Too few options."
            else -> ""
        }
        if (err.isNotBlank()) {
            event.channel.sendMessage(
                error(
                    client,
                    event,
                    err,
                    commandInfo
                )
            ).await().also {
                onComplete(it, true)
            }
            return
        }

        event.channel.sendMessage(
            MessageBuilder()
                .appendLine(argspl.random().trim())
                .build()
        ).await().also {
            onComplete(it, config.auto_delete.bot.content_generation)
        }
    }
}
