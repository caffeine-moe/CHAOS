package org.caffeine.chaos.api.client.connection.handlers

import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.Snowflake
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.client.ClientImpl
import org.caffeine.chaos.api.client.connection.payloads.gateway.guild.delete.GuildDelete
import org.caffeine.chaos.api.json

suspend fun guildDelete(payload : String, client : ClientImpl) {
    val parsed = json.decodeFromString<GuildDelete>(payload)
    val guild = client.userImpl.guilds[Snowflake(parsed.d.id)] ?: return
    client.userImpl.guilds.remove(Snowflake(parsed.d.id))
    client.eventBus.produceEvent(ClientEvent.GuildDelete(guild))
}
