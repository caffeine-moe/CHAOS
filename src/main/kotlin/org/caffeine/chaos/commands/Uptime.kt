package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvents
import org.caffeine.chaos.api.utils.MessageBuilder

import org.caffeine.chaos.api.utils.log
import org.caffeine.chaos.programStartedTime
import java.time.format.DateTimeFormatter
import kotlin.math.absoluteValue

class Uptime : Command(arrayOf("uptime"),
    CommandInfo("Uptime",
        "uptime",
        "Displays how long CHAOS has been running for in Days, Hours, Minutes and Seconds.")) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvents.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) : Unit = coroutineScope {
        val milliseconds = (programStartedTime - System.currentTimeMillis())
        val seconds = ((milliseconds / 1000).toInt() % 60).absoluteValue
        val minutes = (milliseconds / (1000 * 60) % 60).toInt().absoluteValue
        val hours = (milliseconds / (1000 * 60 * 60) % 24).toInt().absoluteValue
        val days = (milliseconds / (1000 * 60 * 60 * 24)).toInt().absoluteValue
        log("CHAOS has been running for $days days, $hours hours, $minutes minutes and $seconds seconds.)")
        event.message.channel.sendMessage(
            MessageBuilder()
            .appendLine("CHAOS has been running for $days days, $hours hours, $minutes minutes and $seconds seconds.")
            .build()).thenAccept {
            this.launch {
                onComplete(it, client, true)
            }
        }
    }
}