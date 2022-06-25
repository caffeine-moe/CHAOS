package org.caffeine.chaos

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientGuildChannel
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.api.client.slashcommands.AppCommand
import org.caffeine.chaos.api.utils.log
import kotlin.math.absoluteValue

class AutoBump : Command(arrayOf("bump", "autobump", "sbump"), CommandInfo("AutoBump", "bump", "Autobumps.")) {
    override suspend fun onCalled(
        client : Client,
        event : MessageCreateEvent,
        args : MutableList<String>,
        cmd : String,
    ) = coroutineScope {
        val pre = "AUTO BUMP:"
        var err = ""
        val logging = client.config.logger.auto_bump
        if (cmd != "sbump") {
            val channel = event.channel

            val guild = client.user.getGuild(channel)

            val guildChannel = if (guild != null) {
                var chan : ClientGuildChannel? = null
                for (c in guild.channels) {
                    if (c.id == channel.id) {
                        chan = c
                    }
                }
                chan
            } else {
                err = "Channel ${channel.id} is not a guild channel."
                null
            }

            var set = AppCommand()

            val command = if (guildChannel != null) {
                if (channel.getAppCommand("bump").isEmpty()) {
                    err = "DISBOARD Bump command not found in channel '${guildChannel.name}' : ${channel.id}"
                } else {
                    for (com in channel.getAppCommand("bump")) {
                        AppCommand()
                        if (com.applicationId == "302050872383242240") {
                            err = ""
                            set = com
                            break
                        } else {
                            err = "DISBOARD Bump command not found in channel '${guildChannel.name}' : ${channel.id}"
                        }
                    }
                }
                set
            } else {
                set
            }

            if (guild != null) {
                for (i in guild.members) {
                    println(i.user.username)
                }
            }

            if (guild != null && !guild.members.stream().filter { x -> x.user.bot }
                    .anyMatch { y -> y.user.id == "302050872383242240" }) {
                err = "DISBOARD bot not found in server."
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
                log("Started bumping in channel '${guildChannel!!.name}' : ${channel.id} in guild ${guild!!.name}",
                    pre)
            }
            while (!autoBumpCock) {
                if (autoBumpCock) break
                channel.sendInteraction(command, guild!!.id).thenAccept {
                    if (logging) {
                        this.launch {
                            log("Bump! in channel '${guildChannel!!.name}' : ${channel.id} in guild ${guild.name}",
                                pre)
                        }
                    }
                }
                val nonce = ((f..l).random()) * 60000
                withContext(Dispatchers.IO) {
                    Thread.sleep(interval + nonce)
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