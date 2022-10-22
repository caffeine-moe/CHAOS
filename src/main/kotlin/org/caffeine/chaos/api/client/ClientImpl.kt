package org.caffeine.chaos.api.client

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collect
import org.caffeine.chaos.api.client.connection.Connection
import org.caffeine.chaos.api.client.connection.ConnectionType
import org.caffeine.chaos.api.client.connection.http.HTTPClient
import org.caffeine.chaos.api.client.user.ClientUser
import org.caffeine.chaos.api.client.user.ClientUserImpl
import org.caffeine.chaos.api.typedefs.ClientType
import org.caffeine.chaos.api.utils.DiscordUtils

class ClientImpl(
    val configuration : ClientConfig,
) : Client {

    override val type : ClientType = configuration.clientType

    var client : Client = this
    val eventBus : EventBus = EventBus()
    val socket : Connection = Connection(this)
    val utils : DiscordUtils = DiscordUtils()
    override val events : SharedFlow<ClientEvent> = eventBus.flow

    val httpClient : HTTPClient = HTTPClient.build(this)

    override suspend fun login() {
        utils.client = this
        socket.execute(ConnectionType.CONNECT)
    }

    override suspend fun logout() {
        socket.execute(ConnectionType.DISCONNECT)
    }

    override suspend fun restart() {
        socket.execute(ConnectionType.RECONNECT)
    }

    lateinit var userImpl : ClientUserImpl
    override lateinit var user : ClientUser
}
