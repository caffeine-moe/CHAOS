package org.caffeine.chaos.api.client

import kotlinx.coroutines.flow.SharedFlow
import org.caffeine.chaos.api.client.connection.Connection
import org.caffeine.chaos.api.client.connection.ConnectionType
import org.caffeine.chaos.api.client.user.ClientUser
import org.caffeine.chaos.api.client.user.ClientUserSettings
import org.caffeine.chaos.api.utils.DiscordUtils

class ClientImpl : BaseClient {
    private val eventBus : EventBus = EventBus()
    override val socket : Connection = Connection(this@ClientImpl, eventBus)
    override val utils : DiscordUtils = DiscordUtils()
    override val events : SharedFlow<ClientEvent> = eventBus.events
    override var user : ClientUser = ClientUser(true, "", "", "", "", "", ClientUserSettings(), "", true, "", this)

    override suspend fun login(token : String) {
    }

    override suspend fun logout() {

    }

    suspend fun setUser(user : ClientUser) {
        this.user = user
    }
}