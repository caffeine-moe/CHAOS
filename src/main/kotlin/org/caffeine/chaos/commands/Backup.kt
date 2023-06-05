package org.caffeine.chaos.commands

import kotlinx.serialization.encodeToString
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.utils.MessageBuilder
import org.caffeine.chaos.api.utils.awaitThen
import org.caffeine.chaos.api.utils.json
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
        val name : String,
        val id : String,
        val vanityUrl : String? = null,
    )

    @kotlinx.serialization.Serializable
    private data class BackupStructure(
        val blockList : List<PrivateUser>,
        val friends : List<PrivateUser>,
        val guilds : List<PrivateGuild>,
    )

    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) {
        val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd_MM_yy_HH_mm_ss"))
        event.message.channel.sendMessage("Performing backup...")
            .awaitThen { message ->
                try {
                    val simpleGuilds =
                        client.user.guilds.values.map { PrivateGuild(it.name, it.id.toString(), it.vanityUrl) }

                    val friends = client.user.friends.values.map {
                        PrivateUser(
                            it.username,
                            it.discriminator,
                            it.id.toString(),
                            it.avatarUrl()
                        )
                    }

                    val blockList = client.user.blocked.values.map {
                        PrivateUser(
                            it.username,
                            it.discriminator,
                            it.id.toString(),
                            it.avatarUrl()
                        )
                    }

                    val textToWrite = json.encodeToString(BackupStructure(blockList, friends, simpleGuilds))

                    val p = File("Backup")
                    if (!p.exists()) p.mkdir()

                    val f : File = if (p.absolutePath.startsWith("/")) {
                        File("${p.absolutePath}/$time.json")
                    } else {
                        File("${p.absolutePath}\\$time.json")
                    }
                    f.writeText(textToWrite)
                    message.edit(
                        MessageBuilder()
                            .appendLine("Backup successful!")
                            .appendLine("Saved to: ${f.absolutePath}")
                    ).awaitThen {
                        onComplete(it, true)
                    }
                } catch (e : Exception) {
                    e.printStackTrace()
                }
            }
    }
}