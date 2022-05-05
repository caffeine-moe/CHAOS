package org.caffeine.chaos.api.client

import org.caffeine.chaos.api.Connection
import org.caffeine.chaos.config.Config

@kotlinx.serialization.Serializable
data class Client(
    var config: Config,
    private val socket: Connection = Connection(),
) {
    lateinit var user: ClientUser
    suspend fun login(config: Config) {
        socket.connect(Client(config, socket))
    }

    suspend fun logout() {
        socket.disconnect()
    }
}
