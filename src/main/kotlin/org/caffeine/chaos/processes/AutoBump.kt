package org.caffeine.chaos.processes
/*
import kotlinx.coroutines.coroutineScope
client.Client
client.ClientEvents
import org.caffeine.octane.utils.log

class AutoBump : Command(arrayOf("bump", "autobump", "sbump"), CommandInfo("AutoBump", "bump", "Autobumps.")) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvents.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) = coroutineScope {
        if (event.message.guild == null) {
            log("AutoBump can only be used in a server.", "AutoBump")
            return@coroutineScope
        }
        val pre = "AUTO BUMP:"
        var err = ""
        val logging = config.logger.auto_bump
        if (cmd != "sbump") {
            val channel = event.message.channel

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
                    println(i.autoDeleteUser.username)
                }
            }

            if (guild != null && !guild.members.stream().filter { x -> x.autoDeleteUser.autoDeleteBot }
                    .anyMatch { y -> y.autoDeleteUser.id == "302050872383242240" }) {
                err = "DISBOARD autoDeleteBot not found in server."
            }

            if (bumping.isNotEmpty() && bumping.any { it.id == event.channel.id }) {
                err = "Already bumping in this channel."
            }

            autoBumpCock = false

            if (config.auto_bump.error.size > 2)
                err = "Config line 47, array is only meant to have two elements."

            val interval = (((config.auto_bump.interval * 60) * 60) * 1000).toLong().absoluteValue

            val f = config.auto_bump.error.first()
            val l = config.auto_bump.error.last()

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
}*/