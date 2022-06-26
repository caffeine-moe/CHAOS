package org.caffeine.chaos.commands

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent

class MuteAllServers : Command(arrayOf("muteallservers", "mas", "muteservers", "muteguilds", "muteallguilds", "mag"),
    CommandInfo("MuteAllServers", "mas", "Mutes all servers indefinitely.")) {
    override suspend fun onCalled(
        client : Client,
        event : MessageCreateEvent,
        args : MutableList<String>,
        cmd : String,
    ) : Unit = coroutineScope {
        val mas = "Muting all servers..."
        event.channel.sendMessage(MessageBuilder()
            .appendLine(mas)
            .build()
        ).thenAccept { message ->
            this.launch {
                val list = client.user.guilds
                val max = list.size
                var done = 0
                suspend fun up() {
                    message.edit(MessageBuilder()
                        .appendLine(mas)
                        .appendLine("$done/$max")
                        .build())
                }
                for (i in list) {
                    i.muteForever()
                    done++
                    up()
                    withContext(Dispatchers.IO) {
                        Thread.sleep(500)
                    }
                }
                message.edit(MessageBuilder()
                    .appendLine("Muted all servers!")
                    .build()
                ).thenAccept {
                    this.launch {
                        onComplete(it, client, true)
                    }
                }
            }
        }
    }
}