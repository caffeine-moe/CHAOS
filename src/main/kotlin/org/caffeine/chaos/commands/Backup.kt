package org.caffeine.chaos.commands

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.json
import org.caffeine.chaos.api.typedefs.MessageBuilder
import java.io.File
import java.nio.file.Files
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Backup :
    Command(
        arrayOf("backup", "bak"),
        CommandInfo("Backup", "backup", "Backs up your discord account in json format.")
    ) {

    @kotlinx.serialization.Serializable
    private data class PrivateUser(
        val username : String,
        val discriminator : String,
        val id : String,
        val avatar : String?,
        val discriminatedName : String = "$username#$discriminator",
    )

    @kotlinx.serialization.Serializable
    private data class PrivateGuild(
        val id : String,
        val name : String,
        val vanityUrl : String?,
    )

    @kotlinx.serialization.Serializable
    private data class BackupStructure(
        val blockList : List<PrivateUser>,
        val friends : List<PrivateUser>,
        val guilds : MutableList<PrivateGuild>,
    )

    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) {
        val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yy_HH:mm:ss"))
        event.message.channel.sendMessage(MessageBuilder().append("Performing backup..."))
            .await().map { message ->
                try {
                    val blockList = mutableListOf<PrivateUser>()

                    val friends = mutableListOf<PrivateUser>()

                    val simpleGuilds = mutableListOf<PrivateGuild>()

                    for (i in client.user.guilds.values.toList()) {
                        simpleGuilds.add(
                            PrivateGuild(name = i.name, id = i.id.asString(), vanityUrl = i.vanityUrl)
                        )
                    }

                    for (i in client.user.friends.values.toList()) {
                        friends.add(
                            PrivateUser(
                                username = i.username,
                                discriminator = i.discriminator,
                                id = i.id.asString(),
                                avatar = i.avatar
                            )
                        )
                    }

                    for (i in client.user.blocked.values.toList()) {
                        blockList.add(
                            PrivateUser(
                                username = i.username,
                                discriminator = i.discriminator,
                                id = i.id.asString(),
                                avatar = i.avatar
                            )
                        )
                    }

                    val textToWrite = json.encodeToString(BackupStructure(blockList, friends, simpleGuilds))
                    val p = File("Backup")
                    if (!p.exists()) {
                        p.mkdir()
                    }
                    var f : File
                    f = File("${p.absolutePath}\\$time.json")
                    if (p.absolutePath.startsWith("/")) {
                        f = File("${p.absolutePath}/$time.json")
                    }
                    withContext(Dispatchers.IO) {
                        Files.createFile(f.toPath())
                    }
                    File(f.toPath().toString()).writeText(textToWrite)
                    try {
                        message.edit(
                            MessageBuilder()
                                .appendLine("Backup successful!")
                                .appendLine("Saved to: ${f.absolutePath}")
                        ).await().orNull().also {
                            if (it != null) {
                                onComplete(it, true)
                            }
                        }
                    } catch (e : Exception) {
                        e.printStackTrace()
                    }
                } catch (e : Exception) {
                    e.printStackTrace()
                }
            }
    }
}
