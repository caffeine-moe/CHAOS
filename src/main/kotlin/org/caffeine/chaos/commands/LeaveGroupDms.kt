package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvents


class LeaveGroupDms :
    Command(
        arrayOf("leavegroupdms", "lgdm", "leavegroups"),
        CommandInfo("LeaveGroupDms", "lgdm", "Leaves all group DMs.")
    ) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvents.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) =
        coroutineScope {
/*            var done = 0
            val channels = StringBuilder()
            try {
                val list = client.user.channels.groupChannels.getList()
                if (list.isEmpty()) {
                    event.channel.sendMessage(MessageBuilder()
                        .appendLine("There are no channels to delete!")
                        .build())
                        .thenAccept { message -> this.launch { onComplete(message, client, true) } }
                    return@coroutineScope
                }
                for (channel : ClientChannel in list) {
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
                        .build())
                        .thenAccept { message -> this.launch { onComplete(message, client, true) } }
                }
                if (done == 1) {
                    log(channels.toString(), "CHANNELS DELETED:")
                    event.channel.sendMessage(MessageBuilder()
                        .appendLine("Done! Deleted $done channel!")
                        .appendLine("Check the console to see the name of the deleted channel.")
                        .build())
                        .thenAccept { message -> this.launch { onComplete(message, client, true) } }
                }
            } catch (e : Exception) {
                println(e.printStackTrace())
            }*/
        }
}