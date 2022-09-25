package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvents
import org.caffeine.chaos.api.utils.MessageBuilder


class MuteAllServers : Command(
    arrayOf("muteallservers", "mas", "muteservers", "muteguilds", "muteallguilds", "mag"),
    CommandInfo("MuteAllServers", "mas", "Mutes all servers indefinitely.")
) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvents.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) : Unit = coroutineScope {
        val mas = "Muting all servers..."
        event.channel.sendMessage(
            MessageBuilder()
                .appendLine(mas)
                .build()
        ).await().also { message ->
            val list = client.user.guilds.values
            val max = list.size
            var done = 0
            suspend fun up() {
                message.edit(
                    MessageBuilder()
                        .appendLine(mas)
                        .appendLine("$done/$max")
                        .build()
                )
            }
            for (i in list) {
                i.muteForever()
                done++
                up()
                delay(500)
            }
            message.edit(
                MessageBuilder()
                    .appendLine("Muted all servers!")
                    .build()
            ).await().also {
                onComplete(it, client, true)
            }
        }
    }
}