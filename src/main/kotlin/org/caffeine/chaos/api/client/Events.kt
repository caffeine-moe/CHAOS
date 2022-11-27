package org.caffeine.chaos.api.client

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.caffeine.chaos.api.client.user.ClientUser
import org.caffeine.chaos.api.entities.guild.GuildImpl
import org.caffeine.chaos.api.entities.channels.TextBasedChannel
import org.caffeine.chaos.api.entities.guild.Guild
import org.caffeine.chaos.api.entities.message.Message


interface ClientEvent {
    class Ready(val user : ClientUser, val time : Long) : ClientEvent
    class MessageCreate(val message : Message, val channel : TextBasedChannel) : ClientEvent
    class GuildCreate(val guild : Guild) : ClientEvent
    class GuildUpdate(val old : Guild, val new : Guild) : ClientEvent

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