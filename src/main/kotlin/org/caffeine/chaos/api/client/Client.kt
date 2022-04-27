package org.caffeine.chaos.api.client

import org.caffeine.chaos.api.Connection
import org.caffeine.chaos.api.ready
import org.caffeine.chaos.config.Config

data class Client(
    var config: Config,
) {
    private val socket = Connection()
    lateinit var user: ClientUser
    suspend fun login(client: Client) {
        this.socket.login(client, config)
    }

    suspend fun logout() {
        ready = false
        this.socket.logout()
    }
}
