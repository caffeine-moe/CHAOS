package org.caffeine.chaos.api

import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.handlers.*
import org.caffeine.chaos.api.payloads.gateway.Default
import org.caffeine.chaos.api.utils.gatewaySequence
import org.caffeine.chaos.log

var ready = false

suspend fun handleJsonRequest(payload : String, connection : Connection, client : Client) {
    val event = json.decodeFromString<Default>(payload)
    if (event.s != null && event.s > 0) {
        gatewaySequence = event.s
    }
    when (event.op) {
        OPCODE.DISPATCH.value -> {
            when (event.t) {
                Event.READY.value -> {
                    ready(client, payload)
                }
                Event.MESSAGE_CREATE.value -> {
                    if (ready) {
                        messageCreate(payload, client)
                    }
                }
                Event.GUILD_DELETE.value -> {
                    if (ready) {
                        guildDelete(payload, client)
                    }
                }
                Event.GUILD_CREATE.value -> {
                    if (ready) {
                        guildCreate(payload, client)
                    }
                }
                Event.GUILD_MEMBER_LIST_UPDATE.value -> {
                    if (ready) {
                        guildMemberListUpdate(payload, client)
                    }
                }
            }
        }
        OPCODE.HEARTBEAT.value -> {
            connection.sendHeartBeat()
        }
        OPCODE.RECONNECT.value -> {
            log("Gateway sent opcode 7 RECONNECT, reconnecting...", "API:")
            connection.execute(ConnectionType.RECONNECT_AND_RESUME, client)
        }
        OPCODE.INVALID_SESSION.value -> {
            log("Client received OPCODE 9 INVALID SESSION, reconnecting...", "API:")
            connection.execute(ConnectionType.RECONNECT, client)
        }
        OPCODE.HEARTBEAT_ACK.value -> {

        }
        else -> {
            println(payload)
        }
    }
}