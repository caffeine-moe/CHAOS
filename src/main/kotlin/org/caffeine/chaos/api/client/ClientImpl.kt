package org.caffeine.chaos.api.client

import kotlinx.coroutines.flow.SharedFlow
import org.caffeine.chaos.api.client.connection.Connection
import org.caffeine.chaos.api.client.connection.ConnectionType
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
    override val events : SharedFlow<ClientEvent> = eventBus.events
    override suspend fun login() {
        utils.client = this
        socket.execute(ConnectionType.CONNECT)
    }

    override suspend fun logout() {
        socket.execute(ConnectionType.DISCONNECT)
    }

    lateinit var userImpl : ClientUserImpl
    override lateinit var user : ClientUser
}
