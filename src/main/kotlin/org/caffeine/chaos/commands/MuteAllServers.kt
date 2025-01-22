package org.caffeine.chaos.commands

import kotlinx.coroutines.delay
import org.caffeine.octane.client.Client
import org.caffeine.octane.client.ClientEvent
import org.caffeine.octane.utils.awaitThen

class MuteAllServers : Command(
    arrayOf("muteallservers", "mas", "muteservers", "muteguilds", "muteallguilds", "mag"),
    CommandInfo("MuteAllServers", "mas", "Mutes all servers indefinitely.")
) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : List<String>,
        cmd : String,
    ) {
        val mas = "Muting all servers..."
        event.channel.sendMessage(mas).awaitThen { message ->
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
            message.edit("Muted all servers!").awaitThen {
                onComplete(it, true)
            }
        }
    }
}