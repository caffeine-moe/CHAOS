package org.caffeine.chaos.api.client.connection.handlers

import SerialGuild
import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.client.ClientImpl
import org.caffeine.chaos.api.client.connection.payloads.gateway.SerialPrivateChannel
import org.caffeine.chaos.api.client.connection.payloads.gateway.ready.Ready
import org.caffeine.chaos.api.client.connection.payloads.gateway.ready.ReadyDRelationship
import org.caffeine.chaos.api.entities.Snowflake
import org.caffeine.chaos.api.entities.asSnowflake
import org.caffeine.chaos.api.entities.channels.DMChannelImpl
import org.caffeine.chaos.api.entities.guild.Guild
import org.caffeine.chaos.api.entities.users.User
import org.caffeine.chaos.api.typedefs.LogLevel
import org.caffeine.chaos.api.typedefs.LoggerLevel
import org.caffeine.chaos.api.utils.ConsoleColour
import org.caffeine.chaos.api.utils.json
import org.caffeine.chaos.api.utils.log

suspend fun ready(client : ClientImpl, payload : String, start : Long) {
    val d = json.decodeFromString<Ready>(payload).d

    client.user = client.utils.createClientUserImpl(d)
    client.user.relationships.putAll(extractRelationships(d.relationships, client))
    client.user.channels.putAll(extractPrivateChannels(d.private_channels, client))
    client.user.guilds.putAll(extractGuilds(d.guilds, client))

    client.socket.sessionId = d.session_id
    client.socket.resumeGatewayUrl = d.resume_gateway_url
    client.socket.ready = true

    val time = System.currentTimeMillis() - start

    log("${ConsoleColour.GREEN.value}Client logged in! ${time}ms", "API:", LogLevel(LoggerLevel.LOW, client))
    client.eventBus.produceEvent(ClientEvent.Ready(client.user, time))
}

fun extractGuilds(guilds : List<SerialGuild>, client : ClientImpl) : Map<Snowflake, Guild> =
    guilds.associateBy({ it.id.asSnowflake() }, { client.utils.createGuild(it) })

private fun extractRelationships(
    relationships : List<ReadyDRelationship>,
    client : ClientImpl,
) : Map<Snowflake, User> =
    relationships.associateBy({ it.user.id.asSnowflake() }, { client.utils.createUser(it.user, it.type) })

private fun extractPrivateChannels(
    channels : List<SerialPrivateChannel>,
    client : ClientImpl,
) : Map<Snowflake, DMChannelImpl> =
    channels.associateBy({ it.id.asSnowflake() }, { client.utils.createDMChannel(it) })