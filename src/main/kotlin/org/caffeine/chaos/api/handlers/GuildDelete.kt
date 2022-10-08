package org.caffeine.chaos.api.handlers

import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.client.ClientEvents
import org.caffeine.chaos.api.client.ClientImpl
import org.caffeine.chaos.api.json
import org.caffeine.chaos.api.payloads.gateway.GuildDelete

suspend fun guildDelete(payload : String, client : ClientImpl) {
    val parsed = json.decodeFromString<GuildDelete>(payload)
    val guild = client.userImpl._guilds[parsed.d.id]
    if (guild != null) {
        client.userImpl._guilds.remove(parsed.d.id)
        client.eventBus.produceEvent(ClientEvents.GuildDelete(guild))
    }
}
