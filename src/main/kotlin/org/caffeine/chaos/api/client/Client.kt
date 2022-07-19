package org.caffeine.chaos.api.client

import kotlinx.coroutines.flow.SharedFlow
import org.caffeine.chaos.api.client.connection.Connection
import org.caffeine.chaos.api.client.connection.ConnectionType
import org.caffeine.chaos.api.client.user.ClientUser
import org.caffeine.chaos.api.client.user.ClientUserSettings
import org.caffeine.chaos.api.utils.DiscordUtils

class Client constructor(private val impl : ClientImpl = ClientImpl()) : BaseClient by impl {
    private val eventBus : EventBus = EventBus()

    override val socket : Connection = Connection(impl, eventBus)
    override val utils : DiscordUtils = DiscordUtils()
    override val events : SharedFlow<ClientEvent> = eventBus.events
    override val user : ClientUser get() = impl.user

    override suspend fun login(token : String) {
        impl.utils.token = token
        impl.utils.client = this@Client
        utils.token = token
        utils.client = this@Client
        socket.execute(ConnectionType.CONNECT)
    }

    override suspend fun logout() {
        socket.execute(ConnectionType.DISCONNECT)
    }
}