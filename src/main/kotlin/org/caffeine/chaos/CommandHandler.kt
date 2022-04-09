package org.caffeine.chaos

import io.ktor.client.features.websocket.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.caffeine.chaos.api.Client
import org.caffeine.chaos.api.messageCreate
import org.caffeine.chaos.api.readypayload
import org.caffeine.chaos.commands.*

suspend fun commandHandler(config: Config, event: messageCreate, ws: DefaultClientWebSocketSession) {
    val client = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }.decodeFromString<Client>(readypayload().payload)
    if (config.nitro_sniper.enabled) {
        NitroSniper(event.d, config)
    }
    //if (event.d.author.id == client.d.user.id) {
    if (event.d.content.toString().startsWith(config.prefix) && event.d.content.toString() != config.prefix) {
        val toFind = event.d.content.toString().replaceFirst(config.prefix, "", true)
        val found = commandlist.any { cmd -> toFind == cmd || toFind.startsWith("$cmd ") }
        if (found) {
            if (config.log_commands) {
                LogV2(event.d.content.toString(), "COMMAND:\u001B[38;5;33m")
            }
            if (config.auto_delete.user) {
                //user(event, config)
            }
            Token(client, event, config)
//                    Help(client, event, config)
//                    Ping(client, event, config)
//                    IP(client, event, config)
//                    Purge(client, event, config)
//                    Avatar(client, event, config)
//                    Spam(event, config)
//                    SSpam(event, config)
//                    Backup(client, event, config)
//                    Exchange(client, event, config)
//                    Coin(client, event, config)
//                    Clear(client, event, config)
//                    SelfDestruct(client, event, config)
//                    LGDM(client, event, config)
            invis(event.d, config)
            dnd(event.d, config)
            online(event.d, config)
            away(event.d, config)
        }
    }
}
