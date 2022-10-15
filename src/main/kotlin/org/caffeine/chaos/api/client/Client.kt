package org.caffeine.chaos.api.client

import kotlinx.coroutines.flow.SharedFlow
import org.caffeine.chaos.api.client.user.ClientUser
import org.caffeine.chaos.api.typedefs.ClientType

interface Client {
    val type : ClientType
    val user : ClientUser
    val events : SharedFlow<ClientEvent>
    suspend fun login()
    suspend fun logout()
}
