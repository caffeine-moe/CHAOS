package org.caffeine.chaos.commands

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import java.io.File
import java.nio.file.Files
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

suspend fun backup(client: Client, event: MessageCreateEvent) = coroutineScope {
    val ftime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yy_hh:mm:ss"))
    if (event.message.content.lowercase() == ("${client.config.prefix}backup")) {
        event.channel.sendMessage(MessageBuilder().append("Performing backup...").build(), client)
            .thenAccept { message ->
                this.launch {
                    val texttowrite = "================================ SERVERS ================================\n" +
                            "${client.user.guilds.getList()}\n" +
                            "================================ FRIENDS ================================\n" +
                            "${client.user.friends.getList()}"
                    val p = File("Backup")
                    if (!p.exists()) {
                        p.mkdir()
                    }
                    if (p.absolutePath.startsWith("/")) {
                        val f = File("${p.absolutePath}/$ftime")
                        withContext(Dispatchers.IO) {
                            Files.createFile(f.toPath())
                        }
                        File(
                            f.toPath().toString()
                        ).writeText(texttowrite)
                        try {
                            message.edit(MessageBuilder()
                                .appendLine("Backup successful!")
                                .appendLine("Saved to: ${f.toPath()}")
                                .build(), client).thenAccept { message ->
                                this.launch {
                                    try {
                                        bot(message, client)
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }
                            }
                            return@launch
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    val f = File("${p.absolutePath}\\$ftime")
                    withContext(Dispatchers.IO) {
                        Files.createFile(f.toPath())
                    }
                    File(
                        f.toPath().toString()
                    ).writeText(texttowrite)
                    message.edit(MessageBuilder()
                        .appendLine("Backup successful!")
                        .appendLine("Saved to: ${f.toPath()}")
                        .build(), client).thenAccept { message ->
                        this.launch {
                            bot(message, client)
                        }
                    }
                }
            }
    }
}
