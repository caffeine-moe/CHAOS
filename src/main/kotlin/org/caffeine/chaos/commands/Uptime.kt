package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.caffeine.chaos.Command
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.programStartedTime
import kotlin.math.absoluteValue

class Uptime : Command(arrayOf("uptime")) {
    override suspend fun onCalled(
        client: Client,
        event: MessageCreateEvent,
        args: MutableList<String>,
        cmd: String,
    ): Unit = coroutineScope {
        val milliseconds = (programStartedTime - System.currentTimeMillis())
        val seconds = ((milliseconds / 1000).toInt() % 60).absoluteValue
        val minutes = (milliseconds / (1000 * 60) % 60).toInt().absoluteValue
        val hours = (milliseconds / (1000 * 60 * 60) % 24).toInt().absoluteValue
        val days = (milliseconds / (1000 * 60 * 60 * 24)).toInt().absoluteValue
        event.channel.sendMessage(MessageBuilder()
            .appendLine("CHAOS has been running for $days days, $hours hours, $minutes minutes and $seconds seconds.")
            .build()).thenAccept {
            this.launch {
                onComplete(it, client, true)
            }
        }
    }
}