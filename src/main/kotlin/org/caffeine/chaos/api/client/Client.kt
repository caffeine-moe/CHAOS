package org.caffeine.chaos.api.client

import org.caffeine.chaos.api.Connection
import org.caffeine.chaos.config.Config

data class Client(
    var config: Config,
) {
    private val socket = Connection()
    lateinit var user: ClientUser
    suspend fun login(config: Config) {
        val client = Client(config)
        socket.connect(client)
    }

    suspend fun logout() {
        this.socket.disconnect()
    }
}
