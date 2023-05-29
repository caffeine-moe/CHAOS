package org.caffeine.chaos.api.client.connection.handlers

import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.client.ClientImpl
import org.caffeine.chaos.api.client.connection.payloads.gateway.guild.create.GuildCreate
import org.caffeine.chaos.api.utils.json

suspend fun guildCreate(payload : String, client : ClientImpl) {
    val parsed = json.decodeFromString<GuildCreate>(payload)
    val guild = client.utils.createGuild(parsed.d)
    client.user.guilds[guild.id] = guild
    client.eventBus.produceEvent(ClientEvent.GuildCreate(guild))
}