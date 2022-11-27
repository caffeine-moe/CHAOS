package org.caffeine.chaos.api.client.connection

import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.GatewayEvent
import org.caffeine.chaos.api.OPCODE
import org.caffeine.chaos.api.client.ClientImpl
import org.caffeine.chaos.api.client.connection.handlers.*
import org.caffeine.chaos.api.client.connection.payloads.gateway.Default
import org.caffeine.chaos.api.json
suspend fun handleJsonRequest(payload : String, client : ClientImpl, start : Long) {
    val event = json.decodeFromString<Default>(payload)
    if (event.s != null) client.utils.gatewaySequence = event.s
    if (event.op == OPCODE.DISPATCH.value && event.t == GatewayEvent.READY.value) ready(client, payload, start)
    if (!client.socket.ready) return
    when (event.op) {
        OPCODE.DISPATCH.value -> {
            when (event.t) {

                GatewayEvent.MESSAGE_CREATE.value -> messageCreate(payload, client)

                GatewayEvent.CHANNEL_CREATE.value -> channelCreate(payload, client)

                GatewayEvent.CHANNEL_UPDATE.value -> channelUpdate(payload, client)

                GatewayEvent.GUILD_DELETE.value -> guildDelete(payload, client)

                GatewayEvent.GUILD_CREATE.value -> guildCreate(payload, client)

                GatewayEvent.GUILD_UPDATE.value -> guildUpdate(payload, client)

                GatewayEvent.GUILD_MEMBER_LIST_UPDATE.value -> guildMemberListUpdate(payload, client)

            }
        }

        OPCODE.HEARTBEAT.value -> client.socket.sendHeartBeat()

        OPCODE.RECONNECT.value -> reconnect(client)

        OPCODE.INVALID_SESSION.value -> invalidSession(client)

        OPCODE.HEARTBEAT_ACK.value -> return

        else -> {
            println(payload)
        }
    }
}
