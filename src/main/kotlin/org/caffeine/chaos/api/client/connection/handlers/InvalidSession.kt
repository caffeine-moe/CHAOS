package org.caffeine.chaos.api.client.connection.handlers

import org.caffeine.chaos.api.client.ClientImpl
import org.caffeine.chaos.api.typedefs.ConnectionType
import org.caffeine.chaos.api.typedefs.LogLevel
import org.caffeine.chaos.api.typedefs.LoggerLevel
import org.caffeine.chaos.api.utils.log

suspend fun invalidSession(client : ClientImpl) {
    log("Client received OPCODE 9 INVALID SESSION, reconnecting...", level = LogLevel(LoggerLevel.LOW, client))
    client.socket.execute(ConnectionType.RECONNECT)
}