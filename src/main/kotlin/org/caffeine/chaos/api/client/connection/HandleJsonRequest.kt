package org.caffeine.chaos.api.client.connection

import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.Event
import org.caffeine.chaos.api.OPCODE
import org.caffeine.chaos.api.client.ClientImpl
import org.caffeine.chaos.api.handlers.guildCreate
import org.caffeine.chaos.api.handlers.guildDelete
import org.caffeine.chaos.api.handlers.messageCreate
import org.caffeine.chaos.api.handlers.ready
import org.caffeine.chaos.api.json
import org.caffeine.chaos.api.payloads.gateway.Default
import org.caffeine.chaos.api.utils.log

suspend fun handleJsonRequest(payload : String, client : ClientImpl) {
    val event = json.decodeFromString<Default>(payload)
    if (event.s != null && event.s > 0) {
        client.utils.gatewaySequence = event.s
    }
    when (event.op) {
        OPCODE.DISPATCH.value -> {
            when (event.t) {
                Event.READY.value -> {
                    ready(client, payload)
                }
                Event.MESSAGE_CREATE.value -> {
                    if (client.ready) {
                        messageCreate(payload, client)
                    }
                }
                Event.GUILD_DELETE.value -> {
                    if (client.ready) {
                        guildDelete(payload, client)
                    }
                }
                Event.GUILD_CREATE.value -> {
                    if (client.ready) {
                        guildCreate(payload, client)
                    }
                }
                Event.GUILD_MEMBER_LIST_UPDATE.value -> {
                    if (client.ready) {
                        //guildMemberListUpdate(payload, client)
                    }
                }
            }
        }
        OPCODE.HEARTBEAT.value -> {
            client.socket.sendHeartBeat()
        }
        OPCODE.RECONNECT.value -> {
            log("Gateway sent opcode 7 RECONNECT, reconnecting...", "API:")
            client.socket.execute(ConnectionType.RECONNECT_AND_RESUME)
        }
        OPCODE.INVALID_SESSION.value -> {
            log("Client received OPCODE 9 INVALID SESSION, reconnecting...", "API:")
            client.socket.execute(ConnectionType.RECONNECT)
        }
        OPCODE.HEARTBEAT_ACK.value -> {

        }
        else -> {
            println(payload)
        }
    }
}