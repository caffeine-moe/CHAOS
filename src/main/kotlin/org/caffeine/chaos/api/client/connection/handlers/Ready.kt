package org.caffeine.chaos.api.client.connection.handlers

import SerialGuild
import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.Snowflake
import org.caffeine.chaos.api.asSnowflake
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.client.ClientImpl
import org.caffeine.chaos.api.client.connection.payloads.gateway.SerialPrivateChannel
import org.caffeine.chaos.api.client.connection.payloads.gateway.ready.Ready
import org.caffeine.chaos.api.client.connection.payloads.gateway.ready.ReadyDRelationship
import org.caffeine.chaos.api.entities.channels.DMChannelImpl
import org.caffeine.chaos.api.entities.guild.Guild
import org.caffeine.chaos.api.entities.users.User
import org.caffeine.chaos.api.json
import org.caffeine.chaos.api.typedefs.LogLevel
import org.caffeine.chaos.api.typedefs.LoggerLevel
import org.caffeine.chaos.api.utils.ConsoleColour
import org.caffeine.chaos.api.utils.log

suspend fun ready(client : ClientImpl, payload : String, start : Long) {
    val d = json.decodeFromString<Ready>(payload).d

    client.userImpl = client.utils.createClientUserImpl(d)
    client.user = client.userImpl

    client.userImpl.relationships = extractRelationships(d.relationships, client)
    client.userImpl.channels.putAll(extractPrivateChannels(d.private_channels, client))
    client.userImpl.guilds.putAll(extractGuilds(d.guilds, client))

    client.utils.sessionId = d.session_id
    client.utils.resumeGatewayUrl = d.resume_gateway_url
    client.socket.ready = true

    val time = System.currentTimeMillis() - start

    log("${ConsoleColour.GREEN.value}Client logged in! ${time}ms", "API:", LogLevel(LoggerLevel.LOW, client))
    client.eventBus.produceEvent(ClientEvent.Ready(client.user, time))
}

fun extractGuilds(guilds : List<SerialGuild>, client : ClientImpl) : HashMap<Snowflake, Guild> =
    guilds.associateBy({ it.id.asSnowflake() }, { client.utils.createGuild(it) }) as HashMap

private fun extractRelationships(
    relationships : List<ReadyDRelationship>,
    client : ClientImpl,
) : HashMap<Snowflake, User> =
    relationships.associateBy({ it.user.id.asSnowflake() }, { client.utils.createUser(it.user, it.type) }) as HashMap

private fun extractPrivateChannels(
    channels : List<SerialPrivateChannel>,
    client : ClientImpl,
) : Map<Snowflake, DMChannelImpl> =
    channels.associateBy({ it.id.asSnowflake() }, { client.utils.createDMChannel(it) })