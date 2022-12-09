package org.caffeine.chaos.commands

import kotlinx.serialization.encodeToString
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.json
import org.caffeine.chaos.api.utils.MessageBuilder
import java.io.File
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
        val avatar : String,
        val discriminatedName : String = "$username#$discriminator",
    )

    @kotlinx.serialization.Serializable
    private data class PrivateGuild(
        val id : String,
        val name : String,
        val vanityUrl : String? = null,
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
            .await().also { message ->
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
                                avatar = i.avatarUrl()
                            )
                        )
                    }

                    for (i in client.user.blocked.values.toList()) {
                        blockList.add(
                            PrivateUser(
                                username = i.username,
                                discriminator = i.discriminator,
                                id = i.id.asString(),
                                avatar = i.avatarUrl()
                            )
                        )
                    }

                    val textToWrite = json.encodeToString(BackupStructure(blockList, friends, simpleGuilds))
                    val p = File("Backup")
                    if (!p.exists()) {
                        p.mkdir()
                    }
                    val f : File = if (p.absolutePath.startsWith("/")) {
                        File("${p.absolutePath}/$time.json")
                    } else {
                        File("${p.absolutePath}\\$time.json")
                    }
                    f.writeText(textToWrite)
                    try {
                        message.edit(
                            MessageBuilder()
                                .appendLine("Backup successful!")
                                .appendLine("Saved to: ${f.absolutePath}")
                        ).await().also {
                            onComplete(it, true)
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
