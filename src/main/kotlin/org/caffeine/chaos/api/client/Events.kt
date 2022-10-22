package org.caffeine.chaos.api.client

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import org.caffeine.chaos.api.client.user.ClientUser
import org.caffeine.chaos.api.models.guild.Guild
import org.caffeine.chaos.api.models.interfaces.TextBasedChannel
import org.caffeine.chaos.api.models.message.Message


interface ClientEvent {
    class Ready(val user : ClientUser) : ClientEvent
    class MessageCreate(val message : Message, val channel : TextBasedChannel) : ClientEvent
    class GuildCreate(val guild : Guild) : ClientEvent
    class GuildDelete(val guild : Guild) : ClientEvent
    object End : ClientEvent
}

class EventBus {
    private val _events = MutableSharedFlow<ClientEvent>() // private mutable shared flow
    val flow = _events.asSharedFlow() // publicly exposed as read-only shared flow

    suspend fun produceEvent(event : ClientEvent) {
        _events.emit(event) // suspends until all subscribers receive it
    }
}