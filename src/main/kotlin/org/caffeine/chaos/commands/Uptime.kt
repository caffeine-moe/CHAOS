package org.caffeine.chaos.commands

import org.caffeine.chaos.programStartedTime
import org.caffeine.octane.client.Client
import org.caffeine.octane.client.ClientEvent
import org.caffeine.octane.utils.awaitThen
import kotlin.math.absoluteValue

class Uptime : Command(
    arrayOf("uptime"),
    CommandInfo(
        "Uptime",
        "uptime",
        "Displays how long CHAOS has been running for in Days, Hours, Minutes and Seconds."
    )
) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : List<String>,
        cmd : String,
    ) {
        val milliseconds = (programStartedTime - System.currentTimeMillis())
        val seconds = ((milliseconds / 1000).toInt() % 60).absoluteValue
        val minutes = (milliseconds / (1000 * 60) % 60).toInt().absoluteValue
        val hours = (milliseconds / (1000 * 60 * 60) % 24).toInt().absoluteValue
        val days = (milliseconds / (1000 * 60 * 60 * 24)).toInt().absoluteValue
        val text = "CHAOS has been running for $days days, $hours hours, $minutes minutes and $seconds seconds."
        event.message.channel.sendMessage(text).awaitThen {
            onComplete(it, false)
        }
    }
}