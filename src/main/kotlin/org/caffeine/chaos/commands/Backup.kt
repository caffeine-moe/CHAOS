package org.caffeine.chaos.commands
/*

import kotlinx.coroutines.coroutineScope
import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client

class Backup :
    Command(arrayOf("backup", "bak"),
        CommandInfo("Backup", "backup", "Backs up your discord account in json format.")) {

    @kotlinx.serialization.Serializable
    data class BackupStructure(
        val blockList : List<ClientBlockedUser>,
        val friends : List<ClientFriend>,
        val guilds : List<ClientGuild>,
    )

    override suspend fun onCalled(
        client : Client,
        event : ClientEvents.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) : Unit = coroutineScope {
*/
/*        val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yy_HH:mm:ss"))
        event.channel.sendMessage(MessageBuilder().append("Performing backup...").build())
            .thenAccept { message ->
                launch {
                    val blockList = client.user.relationships.blockedUsers
                    val friends = client.user.relationships.friends
                    val guilds = client.user.guilds
                    val simpleGuilds = mutableListOf<ClientGuild>()
                    for (i in guilds) {
                        simpleGuilds.add(
                            ClientGuild(i.name, i.id)
                        )
                    }
                    val textToWrite = jsonp.encodeToString(BackupStructure(blockList, friends, simpleGuilds))
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
                        message.edit(MessageBuilder()
                            .appendLine("Backup successful!")
                            .appendLine("Saved to: ${f.absolutePath}")
                            .build()).thenAccept { message ->
                            launch {
                                onComplete(message, client, true)
                            }
                        }
                        return@launch
                    } catch (e : Exception) {
                        e.printStackTrace()
                    }
                }
            }*//*

    }
}*/
