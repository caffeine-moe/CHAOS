package org.caffeine.chaos.api.client.connection.handlers

import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.client.ClientImpl
import org.caffeine.chaos.api.client.connection.payloads.gateway.guild.create.GuildCreate
import org.caffeine.chaos.api.utils.json

/*
@kotlinx.serialization.Serializable
data class Role(
    val color : Int = 0,
    val flags : Int = 0,
    val hoist : Boolean = false,
    val icon : String? = "",
    val id : String = "",
    val managed : Boolean = false,
    val mentionable : Boolean = false,
    val name : String = "",
    val permissions : Int = 0,
    val permissions_new : String = "",
    val position : Int = 0,
    val unicode_emoji : String? = "",
)*/

suspend fun guildCreate(payload : String, client : ClientImpl) {
    val parsed = json.decodeFromString<GuildCreate>(payload)
    val guild = client.utils.createGuild(parsed.d)
    client.user.guilds[guild.id] = guild
    client.eventBus.produceEvent(ClientEvent.GuildCreate(guild))
}