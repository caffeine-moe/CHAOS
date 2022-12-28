package org.caffeine.chaos.api.client

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import org.caffeine.chaos.api.client.config.ClientConfig
import org.caffeine.chaos.api.client.user.ClientUser
import org.caffeine.chaos.api.typedefs.ClientType

interface Client : CoroutineScope {

    val type : ClientType
    val user : ClientUser
    val token : String
    val configuration : ClientConfig
    val events : SharedFlow<ClientEvent>
    suspend fun login()
    suspend fun logout()
    suspend fun restart()
    suspend fun destroy()

    companion object Builder {

        fun config(config : ClientConfig.() -> Unit) : ClientConfig = ClientConfig().also(config)

        fun build(config : Builder.() -> ClientConfig) : Client {
            return ClientImpl(config.invoke(this))
        }
    }
}