package org.caffeine.chaos.commands

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientChannel
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.log


suspend fun lgdm(client: Client, event: MessageCreateEvent) = coroutineScope {
    if (event.message.content.lowercase() == ("${client.config.prefix}lgdm") || event.message.content.lowercase() == ("${client.config.prefix}leavegroups")) {
        var done = 0
        val channels = StringBuilder()
        try {
            val amount = client.user.channels.groupChannels.getList().size
            if (amount <= 0) {
                event.channel.sendMessage(MessageBuilder()
                    .appendLine("There are no channels to delete!")
                    .build(), client)
                    .thenAccept { message -> this.launch { onComplete(message, client, true) } }
                return@coroutineScope
            }
            for (channel: ClientChannel in client.user.channels.groupChannels.getList()) {
                channel.delete()
                channels.append("${channel.name}, ")
                done++
                withContext(Dispatchers.IO) {
                    Thread.sleep(2500)
                }
            }
            if (done > 1) {
                log(channels.toString(), "CHANNELS DELETED:")
                event.channel.sendMessage(MessageBuilder()
                    .appendLine("Done! Deleted $done channels!")
                    .appendLine("Check the console to see a list of the deleted channels.")
                    .build(), client)
                    .thenAccept { message -> this.launch { onComplete(message, client, true) } }
            }
            if (done == 1) {
                log(channels.toString(), "CHANNELS DELETED:")
                event.channel.sendMessage(MessageBuilder()
                    .appendLine("Done! Deleted $done channel!")
                    .appendLine("Check the console to see the name of the deleted channel.")
                    .build(), client)
                    .thenAccept { message -> this.launch { onComplete(message, client, true) } }
            }
        } catch (e: Exception) {
            println(e.printStackTrace())
        }
    }
}