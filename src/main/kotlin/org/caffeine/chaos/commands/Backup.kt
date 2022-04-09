package org.caffeine.chaos.commands

import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.caffeine.chaos.Config
import org.caffeine.chaos.api.BASE_URL
import org.caffeine.chaos.api.httpclient
import org.javacord.api.DiscordApi
import org.javacord.api.entity.message.embed.EmbedBuilder
import org.javacord.api.entity.server.Server
import org.javacord.api.event.message.MessageCreateEvent
import java.io.File
import java.nio.file.Files
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.concurrent.thread

@Serializable
data class gfr(
    val user: fusr,
)

@Serializable
data class fusr(
    val username: String,
    val discriminator: Int,
)

@Serializable
data class ggl(
    val name: String
)

private val fr = StringBuilder()

fun Backup(client: DiscordApi, event: MessageCreateEvent, config: Config) {
    thread {
        val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yy hh:mm:ss"))
        val ftime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd|MM|yy_hh:mm:ss"))
        if (event.messageContent.lowercase() == ("${config.prefix}backup")) {
            event.channel.sendMessage(EmbedBuilder().setTitle("Preforming backup...").setFooter(time))
                .thenAccept { message ->
                    val p = File("Backup")
                    if (!p.exists()) {
                        p.mkdir()
                    }
                    if (p.absolutePath.startsWith("/")) {
                        val f = File("${p.absolutePath}/$ftime")
                        Files.createFile(f.toPath())
                        val se = StringBuilder()
                        for (server: Server in client.servers) {
                            se.appendLine(server.name)
                        }
                        getfriends(config = config)
                        File(
                            f.toPath().toString()
                        ).writeText("================================ SERVERS ================================\n$se\n================================ FRIENDS ================================\n${fr.trim()}")
                        message.edit(
                            EmbedBuilder()
                                .setTitle("Backup successful!")
                                .addInlineField("Saved to:", f.toPath().toString())
                                .setFooter(time)
                        )
                        return@thenAccept
                    }
                    val f = File("${p.absolutePath}\\$ftime")
                    Files.createFile(f.toPath())
                    val se = StringBuilder()
                    for (server: Server in client.servers) {
                        se.appendLine(server.name)
                    }
                    File(
                        f.toPath().toString()
                    ).writeText("================================ SERVERS ================================\n$se\n================================ FRIENDS ================================\n${fr.trim()}}")
                    message.edit(
                        EmbedBuilder()
                            .setTitle("Backup successful!")
                            .addInlineField("Saved to:", f.toPath().toString())
                            .setFooter(time)
                    )
                }
        }
    }
}

fun getfriends(config: Config) {
    runBlocking {
        try {
            val response = httpclient.request<String>("$BASE_URL/users/@me/relationships") {
                method = HttpMethod.Get
                headers {
                    append(HttpHeaders.Authorization, config.token)
                }
            }
            val final = Json { ignoreUnknownKeys = true }.decodeFromString<List<gfr>>(response)
            for ((count) in final.withIndex()) {
                fr.appendLine("${final[count].user.username}#${final[count].user.discriminator}")
            }
        } catch (e: Exception) {
            println(e)
        }
    }
}