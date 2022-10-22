package org.caffeine.chaos.api.client.connection.handlers

import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.client.ClientImpl
import org.caffeine.chaos.api.client.connection.payloads.gateway.guild.delete.GuildDelete
import org.caffeine.chaos.api.json

suspend fun guildDelete(payload : String, client : ClientImpl) {
    val parsed = json.decodeFromString<GuildDelete>(payload)
    val guild = client.userImpl.guilds[parsed.d.id]
    if (guild != null) {
        client.userImpl.guilds.remove(parsed.d.id)
        client.eventBus.produceEvent(ClientEvent.GuildDelete(guild))
    }
}
