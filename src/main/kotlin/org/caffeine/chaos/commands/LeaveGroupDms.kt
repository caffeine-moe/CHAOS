package org.caffeine.chaos.commands

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.caffeine.octane.client.Client
import org.caffeine.octane.client.ClientEvent
import org.caffeine.octane.typedefs.ChannelType
import org.caffeine.octane.utils.MessageBuilder
import org.caffeine.octane.utils.awaitThen
import org.caffeine.octane.utils.log

class LeaveGroupDms :
    Command(
        arrayOf("leavegroupdms", "lgdm", "leavegroups"),
        CommandInfo("LeaveGroupDms", "lgdm", "Leaves all group DMs.")
    ) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : List<String>,
        cmd : String,
    ) {
        var done = 0
        val channels = StringBuilder()
        try {
            val list = client.user.channels.values.filter { it.type == ChannelType.GROUP }
            if (list.isEmpty()) {
                event.channel.sendMessage("There are no channels to delete!")
                    .awaitThen { message -> onComplete(message, true) }
                return
            }
            list.map { channel ->
                channel.delete()
                channels.append("${channel.name}, ")
                done++
                withContext(Dispatchers.IO) {
                    Thread.sleep(2500)
                }
            }
            val s = if (done > 1) "s" else ""
            log(channels.toString(), "CHANNELS DELETED:")
            event.channel.sendMessage(
                MessageBuilder()
                    .appendLine("Done! Deleted $done channel$s!")
                    .appendLine("Check the console to see the name$s of the deleted channel$s.")
            ).awaitThen { message -> onComplete(message, true) }
        } catch (e : Exception) {
            println(e.printStackTrace())
        }
    }
}