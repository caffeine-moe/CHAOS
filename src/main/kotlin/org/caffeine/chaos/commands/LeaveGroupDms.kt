package org.caffeine.chaos.commands

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvents
import org.caffeine.chaos.api.models.interfaces.BaseChannel
import org.caffeine.chaos.api.typedefs.ChannelType
import org.caffeine.chaos.api.utils.MessageBuilder
import org.caffeine.chaos.api.utils.log

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
    ) {
        var done = 0
        val channels = StringBuilder()
        try {
            val list = client.user.channels.values.filter { it.type == ChannelType.GROUP }
            if (list.isEmpty()) {
                event.channel.sendMessage(
                    MessageBuilder()
                        .appendLine("There are no channels to delete!")
                        .build()
                )
                    .await().also { message -> onComplete(message, true) }
                return
            }
            for (channel : BaseChannel in list) {
                channel.delete()
                channels.append("${channel.name}, ")
                done++
                withContext(Dispatchers.IO) {
                    Thread.sleep(2500)
                }
            }
            if (done > 1) {
                log(channels.toString(), "CHANNELS DELETED:")
                event.channel.sendMessage(
                    MessageBuilder()
                        .appendLine("Done! Deleted $done channels!")
                        .appendLine("Check the console to see a list of the deleted channels.")
                        .build()
                )
                    .await().also { message -> onComplete(message, true) }
            }
            if (done == 1) {
                log(channels.toString(), "CHANNELS DELETED:")
                event.channel.sendMessage(
                    MessageBuilder()
                        .appendLine("Done! Deleted $done channel!")
                        .appendLine("Check the console to see the name of the deleted channel.")
                        .build()
                )
                    .await().also { message -> onComplete(message, true) }
            }
        } catch (e : Exception) {
            println(e.printStackTrace())
        }
    }
}
