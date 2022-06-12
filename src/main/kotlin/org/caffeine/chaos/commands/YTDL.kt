package org.caffeine.chaos.commands

import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageCreateEvent

class YTDL : Command(arrayOf("ytdl", "youtubedl"),
    CommandInfo("YoutubeDL", "ytdl <URL> [format]", "Downloads YouTube videos.")) {
    override suspend fun onCalled(
        client : Client,
        event : MessageCreateEvent,
        args : MutableList<String>,
        cmd : String,
    ) {

    }
}