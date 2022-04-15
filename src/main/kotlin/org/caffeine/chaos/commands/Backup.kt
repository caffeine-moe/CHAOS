package org.caffeine.chaos.commands

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.caffeine.chaos.Config
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import java.io.File
import java.nio.file.Files
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

suspend fun Backup(client: Client, event: MessageCreateEvent, config: Config) = coroutineScope {
    val ftime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yy_hh:mm:ss"))
    if (event.message.content.lowercase() == ("${config.prefix}backup")) {
        event.message.channel.sendMessage(MessageBuilder().append("Performing backup...").build(), config, client)
            .thenAccept { message ->
                this.launch {
                    val texttowrite = "================================ SERVERS ================================\n" +
                            "${client.user.guilds.getList(config)}\n" +
                            "================================ FRIENDS ================================\n" +
                            "${client.user.friends.getList(config)}"
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
                        try{
                        message.edit(MessageBuilder()
                            .appendLine("Backup successful!")
                            .appendLine("Saved to: ${f.toPath()}")
                            .build(), config).thenAccept { message ->
                            this.launch {
                                try {
                                    bot(message, config)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }
                            return@launch
                    }catch (e: Exception){
                    e.printStackTrace()}
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
                        .build(), config).thenAccept { message ->
                        this.launch {
                            bot(message, config)
                        }
                    }
                }
            }
    }
}
