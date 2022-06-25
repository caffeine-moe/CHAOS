package org.caffeine.chaos.commands

import com.github.kiulian.downloader.Config
import com.github.kiulian.downloader.YoutubeDownloader
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.api.utils.normalHTTPClient
import java.io.File
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.concurrent.Executors


class YTDL : Command(arrayOf("ytdl", "youtubedl"),
    CommandInfo("YoutubeDL", "ytdl <URL> [format]", "Downloads YouTube videos.")) {
    override suspend fun onCalled(
        client : Client,
        event : MessageCreateEvent,
        args : MutableList<String>,
        cmd : String,
    ) = coroutineScope {
        if (args.isNotEmpty()) {
            val regex =
                "^((?:https?:)?//)?((?:www|m)\\.)?(youtube(-nocookie)?\\.com|youtu.be)(/(?:[\\w\\-]+\\?v=|embed/|v/)?)([\\w\\-]+)(\\S+)?\$".toRegex()
            if (regex.matches(args.first())) {
                val id = regex.matchEntire(args.first())!!.groups[6]!!.value
                val config = Config.Builder()
                    .executorService(Executors.newCachedThreadPool()) // for async requests, default Executors.newCachedThreadPool()
                    .maxRetries(1) // retry on failure, default 0
                    .header("Accept-language", "en-US,en;") // extra request header
                    .build()
                val downloader = YoutubeDownloader(config)
                config.maxRetries = 0
                val request = RequestVideoInfo(id)
                val infoResponse = downloader.getVideoInfo(request)
                val videoData = infoResponse.data()

                val regexVideoName = videoData.details().title().replace("[^A-z ]".toRegex(), "").replace(" ", "_")

                if (client.config.ytdl.download) {
                    val filename = "${regexVideoName}.mp4"
                    withContext(Dispatchers.IO) {
                        val inputStream = URL(videoData.bestVideoWithAudioFormat().url()).openStream()
                        Files.copy(inputStream,
                            Paths.get(filename),
                            StandardCopyOption.REPLACE_EXISTING)
                    }

                    val file = (File(filename).absoluteFile)

                    val filepath = file.absolutePath

                    event.channel.sendMessage(MessageBuilder()
                        .appendLine("downloaded ${args.first()} to `$filepath`")
                        .build()
                    ).thenAccept {
                        launch {
                            onComplete(it, client, true)
                        }
                    }
                }

                if (client.config.ytdl.upload) {
                    val vsize = (withContext(Dispatchers.IO) {
                        val re = normalHTTPClient.head(videoData.bestVideoWithAudioFormat().url())
                        re.headers[HttpHeaders.ContentLength]!!.toLong()
                    })
                    if (vsize / (1024 * 1024) <= 512) {
                        val rsp = HttpClient().request("https://0x0.st") {
                            method = HttpMethod.Post
                            setBody(MultiPartFormDataContent(
                                formData {
                                    append("url", videoData.bestVideoWithAudioFormat().url())
                                }
                            ))
                        }
                        event.channel.sendMessage(MessageBuilder()
                            .appendLine("uploaded <${args.first()}> to ${rsp.bodyAsText()}")
                            .build()
                        ).thenAccept {
                            launch {
                                onComplete(it, client, client.config.auto_delete.bot.content_generation)
                            }
                        }
                    } else {
                        event.channel.sendMessage(error(client,
                            event,
                            "Video is too big to be uploaded. (${vsize / (1024 * 1024)}MB)",
                            commandInfo))
                            .thenAccept {
                                launch {
                                    onComplete(it, client, true)
                                }
                            }
                    }
                }

                return@coroutineScope
            } else {
                event.channel.sendMessage(error(client, event, "Invalid YouTube URL '${args.first()}'", commandInfo))
                    .thenAccept {
                        launch {
                            onComplete(it, client, true)
                        }
                    }
            }
        }
    }
}