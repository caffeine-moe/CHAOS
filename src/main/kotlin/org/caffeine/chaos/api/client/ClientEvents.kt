package org.caffeine.chaos.api.client

import org.caffeine.chaos.api.client.user.ClientUser
import org.caffeine.chaos.api.models.Message

interface ClientEvent

abstract class ClientEvents {
    class Ready(val user : ClientUser) : ClientEvent
    class MessageCreate (val message: Message = Message()) : ClientEvent
}