package org.caffeine.chaos.api.client

import kotlinx.coroutines.flow.SharedFlow
import org.caffeine.chaos.api.client.connection.Connection
import org.caffeine.chaos.api.client.connection.ConnectionType
import org.caffeine.chaos.api.client.user.ClientUser
import org.caffeine.chaos.api.client.user.ClientUserSettings
import org.caffeine.chaos.api.utils.DiscordUtils

class Client (private val impl : ClientImpl = ClientImpl()) : BaseClient by impl {
    override suspend fun login(token : String) {
        impl.client = this
        utils.token = token
        utils.client = this
        socket.execute(ConnectionType.CONNECT)
    }

    override suspend fun logout() {
        socket.execute(ConnectionType.DISCONNECT)
    }
}