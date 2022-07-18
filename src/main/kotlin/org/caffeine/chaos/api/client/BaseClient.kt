package org.caffeine.chaos.api.client

import kotlinx.coroutines.flow.SharedFlow
import org.caffeine.chaos.api.client.connection.Connection
import org.caffeine.chaos.api.client.user.ClientUser
import org.caffeine.chaos.api.utils.DiscordUtils

internal interface BaseClient {
    val user : ClientUser
    val events : SharedFlow<ClientEvent>
    //val relationships: HashMap<String, ClientRelationship>
    val socket : Connection
    val utils : DiscordUtils
    suspend fun login(token : String)
    suspend fun logout()
    suspend fun setUser(user : ClientUser)
}