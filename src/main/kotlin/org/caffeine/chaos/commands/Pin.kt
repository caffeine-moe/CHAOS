package org.caffeine.chaos.commands

import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvents


class Pin : Command(
    arrayOf("pin", "p"),
    CommandInfo(
        "Pin",
        "pin <Message OR Reply>",
        "Pins text that you put after the command OR a message that you reply to."
    )
) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvents.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) {
        /*                if (event.message.referenced_message != null) {
                            val refmes = event.message.referenced_message!!
                            when (refmes.pinned) {
                                true -> refmes.unpin()
                                false -> refmes.pin()
                            }
                            return
                        }
                        if (args.isEmpty()) {
                            log("Unable to pin message, no arguments."); return
                        }
                        event.channel.sendMessage(MessageBuilder()
                            .appendLine(args.joinToString(" "))
                            .build()
                        ).thenAccept {
                            this.launch {
                                it.pin()
                            }
                        }*/
    }
}