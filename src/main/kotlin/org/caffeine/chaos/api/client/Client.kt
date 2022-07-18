package org.caffeine.chaos.api.client

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.caffeine.chaos.api.client.connection.Connection
import org.caffeine.chaos.api.client.connection.ConnectionType
import org.caffeine.chaos.api.client.user.ClientUser
import org.caffeine.chaos.api.client.user.ClientUserSettings
import org.caffeine.chaos.api.utils.DiscordUtils

class EventBus {
    private val _events = MutableSharedFlow<ClientEvent>() // private mutable shared flow
    val events = _events.asSharedFlow() // publicly exposed as read-only shared flow

    suspend fun produceEvent(event : ClientEvent) {
        _events.emit(event) // suspends until all subscribers receive it
    }
}

class Client : BaseClient {

    private var _user : ClientUser = ClientUser(true, "", "", "", "", "", ClientUserSettings(), "", true, "", this)
    private val eventBus : EventBus = EventBus()

    override val socket : Connection = Connection(this, eventBus)
    override val utils : DiscordUtils = DiscordUtils()
    override val events : SharedFlow<ClientEvent> = eventBus.events
    override val user : ClientUser get() = _user

    override suspend fun login(token : String) {
        utils.token = token
        utils.client = this@Client
        socket.execute(ConnectionType.CONNECT)
    }

    override suspend fun logout() {
        socket.execute(ConnectionType.DISCONNECT)
    }

    override suspend fun setUser(user : ClientUser) {
        _user = user
    }

}