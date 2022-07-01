package org.caffeine.chaos.api

import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.EventBus
import org.caffeine.chaos.api.client.connection.ConnectionType
import org.caffeine.chaos.api.handlers.*
import org.caffeine.chaos.api.payloads.gateway.Default
import org.caffeine.chaos.api.utils.log

suspend fun handleJsonRequest(payload : String, client : Client, eventBus : EventBus) {
    val event = json.decodeFromString<Default>(payload)
    if (event.s != null && event.s > 0) {
        client.rest.gatewaySequence = event.s
    }
    when (event.op) {
        OPCODE.DISPATCH.value -> {
            when (event.t) {
                Event.READY.value -> {
                    ready(client, payload, eventBus)
                }
                Event.MESSAGE_CREATE.value -> {
                    if (client.socket.ready) {
                        messageCreate(payload, client, eventBus)
                    }
                }
                Event.GUILD_DELETE.value -> {
                    if (client.socket.ready) {
                        guildDelete(payload, client)
                    }
                }
                Event.GUILD_CREATE.value -> {
                    if (client.socket.ready) {
                        guildCreate(payload, client)
                    }
                }
                Event.GUILD_MEMBER_LIST_UPDATE.value -> {
                    if (client.socket.ready) {
                        guildMemberListUpdate(payload, client)
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