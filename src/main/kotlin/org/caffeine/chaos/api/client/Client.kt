package org.caffeine.chaos.api.client

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.caffeine.chaos.api.client.connection.Connection
import org.caffeine.chaos.api.client.connection.ConnectionType
import org.caffeine.chaos.api.utils.DiscordUtils

interface  Event

abstract class ClientEvents {
    object Ready : Event
}

class EventBus {
    private val _events = MutableSharedFlow<Event>() // private mutable shared flow
    val events = _events.asSharedFlow() // publicly exposed as read-only shared flow

    suspend fun produceEvent(event : Event) {
        _events.emit(event) // suspends until all subscribers receive it
    }
}

private interface BaseClient {
    val user: ClientUser
    val events: SharedFlow<Event>
    //val guilds: HashMap<String, Guild>
    //val channels: HashMap<String, BaseChannel>
    //val relationships: HashMap<String, ClientRelationship>
    val socket: Connection
    val rest: DiscordUtils
    suspend fun login(token: String)
    suspend fun logout()
}

class Client : BaseClient {

    private val eventBus: EventBus = EventBus()
    override val socket : Connection = Connection(this, eventBus)
    override val rest : DiscordUtils = DiscordUtils()
    override val events : SharedFlow<Event> = eventBus.events

    override lateinit var user : ClientUser

    override suspend fun login(token: String) {
        rest.token = token
        socket.execute(ConnectionType.CONNECT)
    }

    override suspend fun logout() {
        socket.execute(ConnectionType.DISCONNECT)
    }

}