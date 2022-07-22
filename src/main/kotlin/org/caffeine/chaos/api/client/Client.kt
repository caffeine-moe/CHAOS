package org.caffeine.chaos.api.client

import org.caffeine.chaos.api.client.connection.ConnectionType

private val impl : ClientImpl = ClientImpl()

class Client : BaseClient by impl {

    override suspend fun login(token : String) {
        impl.client = this
        utils.token = token
        utils.client = this
        impl.socket.execute(ConnectionType.CONNECT)
    }

    override suspend fun logout() {
        impl.socket.execute(ConnectionType.DISCONNECT)
    }
}