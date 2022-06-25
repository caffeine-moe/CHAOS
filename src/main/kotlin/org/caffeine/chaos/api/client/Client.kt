package org.caffeine.chaos.api.client

import org.caffeine.chaos.api.Connection
import org.caffeine.chaos.api.ConnectionType
import org.caffeine.chaos.config.Config

class Client(
    var config : Config,
    val socket : Connection = Connection(),
) {
    lateinit var user : ClientUser
    suspend fun login() {
        socket.execute(ConnectionType.CONNECT, this)
    }

    suspend fun logout() {
        socket.execute(ConnectionType.DISCONNECT, this)
    }
}