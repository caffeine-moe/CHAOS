package org.caffeine.chaos

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import kotlin.math.absoluteValue

class AutoBump : Command(arrayOf("bump", "autobump", "sbump"), CommandInfo("AutoBump", "bump", "Autobumps.")) {
    override suspend fun onCalled(
        client : Client,
        event : MessageCreateEvent,
        args : MutableList<String>,
        cmd : String,
    ) =
        coroutineScope {
            val pre = "AUTO BUMP:"
            var err = ""
            val logging = client.config.logger.auto_bump
            if (cmd != "sbump") {

                val channel = event.channel

                var guildId = ""

                if (event.channel.getGuild() != null) {
                    guildId = event.channel.getGuild()!!.id
                } else {
                    err = "Channel ${channel.id} is not in a guild."
                }

                if (bumping.isNotEmpty() && bumping.any { it.id == event.channel.id }) {
                    err = "Already bumping in this channel."
                }
                autoBumpCock = false

                if (client.config.auto_bump.error.size > 2)
                    err = "Config line 47, array is only meant to have two elements."

                val interval = (((client.config.auto_bump.interval * 60) * 60) * 1000).toLong().absoluteValue

                val f = client.config.auto_bump.error.first()
                val l = client.config.auto_bump.error.last()

                if (err.isNotBlank()) {
                    if (logging) {
                        log(err, pre)
                    }
                    return@coroutineScope
                }
                bumping.add(channel)
                if (logging) {
                    log("Started bumping in channel ${channel.id}", pre)
                }
                while (!autoBumpCock) {
                    val nonce = ((f..l).random()) * 60000
                    withContext(Dispatchers.IO) {
                        Thread.sleep(interval + nonce)
                    }
                    if (autoBumpCock) break
                    channel.sendInteraction("bump", guildId).thenAccept {
                        if (logging) {
                            this.launch {
                                log("Bump! in channel ${channel.id}", pre)
                            }
                        }
                    }
                }
                return@coroutineScope
            }
            if (logging) {
                log("Stopped bumping.", pre)
            }
            autoBumpCock = true
            bumping = mutableListOf()
        }
}