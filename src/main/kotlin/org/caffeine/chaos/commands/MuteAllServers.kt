package org.caffeine.chaos.commands

import kotlinx.coroutines.delay
import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.typedefs.MessageBuilder

class MuteAllServers : Command(
    arrayOf("muteallservers", "mas", "muteservers", "muteguilds", "muteallguilds", "mag"),
    CommandInfo("MuteAllServers", "mas", "Mutes all servers indefinitely.")
) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) {
        val mas = "Muting all servers..."
        event.channel.sendMessage(mas).await().map { message ->
            val list = client.user.guilds.values
            val max = list.size
            var done = 0
            list.forEach {
                it.muteForever()
                done++
                message.edit(
                    "$mas\n$done/$max"
                ).await()
                delay(500)
            }
            message.edit("Muted all servers!").await().map {
                onComplete(it, true)
            }
        }
    }
}
