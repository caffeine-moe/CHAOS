package org.caffeine.chaos.commands

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import org.caffeine.chaos.Command
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientFriend
import org.caffeine.chaos.api.client.ClientGuild
import org.caffeine.chaos.api.client.DiscordUser
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.api.jsonp
import java.io.File
import java.nio.file.Files
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Backup : Command(arrayOf("backup")) {

    @kotlinx.serialization.Serializable
    data class BackupStructure(
        val blocklist: List<DiscordUser>,
        val friends: List<ClientFriend>,
        val guilds: List<ClientGuild>,
    )

    override suspend fun onCalled(
        client: Client,
        event: MessageCreateEvent,
        args: MutableList<String>,
        cmd: String,
    ): Unit = coroutineScope {
        val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yy_HH:mm:ss"))
        event.channel.sendMessage(MessageBuilder().append("Performing backup...").build())
            .thenAccept { message ->
                launch {
                    val blocklist = client.user.relationships.blockedUsers.getList()
                    val friends = client.user.relationships.friends.getList()
                    val guilds = client.user.guilds.getList()
                    val textToWrite = jsonp.encodeToString(BackupStructure(blocklist, friends, guilds))
                    val p = File("Backup")
                    if (!p.exists()) {
                        p.mkdir()
                    }
                    var f: File
                    f = File("${p.absolutePath}\\$time.json")
                    if (p.absolutePath.startsWith("/")) {
                        f = File("${p.absolutePath}/$time.json")
                    }
                    withContext(Dispatchers.IO) {
                        Files.createFile(f.toPath())
                    }
                    File(f.toPath().toString()).writeText(textToWrite)
                    try {
                        message.edit(MessageBuilder()
                            .appendLine("Backup successful!")
                            .appendLine("Saved to: ${f.absolutePath}")
                            .build()).thenAccept { message ->
                            launch {
                                onComplete(message, client, true)
                            }
                        }
                        return@launch
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
    }
}
