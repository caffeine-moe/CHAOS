package org.caffeine.chaos.api.client.connection

import org.caffeine.chaos.api.client.ClientImpl
import org.caffeine.chaos.api.client.connection.handlers.*
import org.caffeine.chaos.api.client.connection.payloads.gateway.Default
import org.caffeine.chaos.api.utils.tryDecodeFromString

suspend fun handleJsonRequest(payload : String, client : ClientImpl) {
    val event = tryDecodeFromString<Default>(payload) ?: return
    client.socket.gatewaySequence = event.s ?: client.socket.gatewaySequence
    when (event.op) {
        OPCODE.DISPATCH.value -> {
            if (!isReady(event, client, payload)) return
            when (event.t) {

                GatewayEvent.MESSAGE_CREATE.value -> messageCreate(payload, client)

                GatewayEvent.MESSAGE_UPDATE.value -> messageUpdate(payload, client)

                GatewayEvent.MESSAGE_DELETE.value -> messageDelete(payload, client)

                GatewayEvent.CHANNEL_CREATE.value -> channelMod(payload, client)

                GatewayEvent.CHANNEL_UPDATE.value -> channelMod(payload, client)

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

private suspend fun isReady(event : Default, client : ClientImpl, payload : String) : Boolean {
    if (event.op == OPCODE.DISPATCH.value && event.t == GatewayEvent.READY.value) ready(client, payload)
    return client.socket.ready
}