package org.caffeine.chaos.api.client

import kotlinx.coroutines.flow.SharedFlow
import org.caffeine.chaos.api.client.user.ClientUser

internal interface BaseClient {
    val user : ClientUser
    val events : SharedFlow<ClientEvent>
    suspend fun login(token : String)
    suspend fun logout()
}