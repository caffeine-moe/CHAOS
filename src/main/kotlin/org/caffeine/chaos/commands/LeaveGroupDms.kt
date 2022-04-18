package org.caffeine.chaos.commands

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.caffeine.chaos.Config
import org.caffeine.chaos.log
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientChannel
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent


suspend fun LGDM(client: Client, event: MessageCreateEvent, config: Config) = coroutineScope {
        if (event.message.content.lowercase() == ("${config.prefix}lgdm") || event.message.content.lowercase() == ("${config.prefix}leavegroups")) {
            var done = 0
            val channels = StringBuilder()
                try {
                    val amount = client.user.channels.groupChannels.getList(config).size
                    if (amount <= 0) {
                        event.channel.sendMessage(MessageBuilder()
                            .appendLine("There are no channels to delete!")
                            .build(), config, client)
                            .thenAccept { message -> this.launch { bot(message, config) } }
                        return@coroutineScope
                    }
                    for (channel: ClientChannel in client.user.channels.groupChannels.getList(config)) {
                        channel.delete(config)
                        channels.append("${channel.name}, ")
                        done ++
                        withContext(Dispatchers.IO) {
                            Thread.sleep(1000)
                        }
                    }
                    if (done > 1) {
                        log(channels.toString(), "CHANNELS DELETED:")
                        event.channel.sendMessage(MessageBuilder()
                            .appendLine("Done! Deleted $done channels!")
                            .appendLine("Check the console to see a list of the deleted channels.")
                            .build(), config, client)
                            .thenAccept { message -> this.launch { bot(message, config) } }
                    }
                    if (done == 1){
                        log(channels.toString(), "CHANNELS DELETED:")
                        event.channel.sendMessage(MessageBuilder()
                            .appendLine("Done! Deleted $done channel!")
                            .appendLine("Check the console to see the name of the deleted channel.")
                            .build(), config, client)
                            .thenAccept { message -> this.launch { bot(message, config) } }
                    }
                } catch (e: Exception) {
                    println(e.printStackTrace())
                }
            }
}