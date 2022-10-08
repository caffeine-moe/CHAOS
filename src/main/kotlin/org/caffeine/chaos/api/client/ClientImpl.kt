package org.caffeine.chaos.api.client

import kotlinx.coroutines.flow.SharedFlow
import org.caffeine.chaos.api.client.connection.Connection
import org.caffeine.chaos.api.client.user.ClientUser
import org.caffeine.chaos.api.client.user.ClientUserImpl
import org.caffeine.chaos.api.utils.DiscordUtils

class ClientImpl : BaseClientImpl {

    lateinit var client : Client
    var ready : Boolean = false
    override val eventBus : EventBus = EventBus()
    override val socket : Connection = Connection(this)
    override val utils : DiscordUtils = DiscordUtils()
    override val events : SharedFlow<ClientEvent> = eventBus.events
    lateinit var userImpl : ClientUserImpl
    override lateinit var user : ClientUser

    override suspend fun login(token : String) {}

    override suspend fun logout() {}
}
