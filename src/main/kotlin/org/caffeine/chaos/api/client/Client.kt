package org.caffeine.chaos.api.client

import org.caffeine.chaos.api.client.connection.ConnectionType

private val impl : ClientImpl = ClientImpl()

class Client : BaseClient by impl {

    override suspend fun login(token : String) {
        impl.client = this
        impl.utils.token = token
        impl.utils.client = impl
        impl.socket.execute(ConnectionType.CONNECT)
    }

    override suspend fun logout() {
        impl.socket.execute(ConnectionType.DISCONNECT)
    }
}
