package org.caffeine.chaos.api.client.connection.handlers

import org.caffeine.chaos.api.client.ClientImpl
import org.caffeine.chaos.api.typedefs.ClientType
import org.caffeine.chaos.api.typedefs.ConnectionType
import org.caffeine.chaos.api.typedefs.LogLevel
import org.caffeine.chaos.api.typedefs.LoggerLevel
import org.caffeine.chaos.api.utils.log

suspend fun reconnect(client : ClientImpl) {
    log("Gateway sent opcode 7 RECONNECT, reconnecting...", "API:", LogLevel(LoggerLevel.LOW, client))
    if (client.type != ClientType.BOT) {
        client.socket.execute(ConnectionType.CONNECT)
        return
    }
    client.socket.execute(ConnectionType.RECONNECT_AND_RESUME)
}