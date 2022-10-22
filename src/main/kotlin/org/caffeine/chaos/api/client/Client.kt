package org.caffeine.chaos.api.client

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import org.caffeine.chaos.api.client.user.ClientUser
import org.caffeine.chaos.api.typedefs.ClientType
import org.caffeine.chaos.api.typedefs.LogLevel
import org.caffeine.chaos.api.typedefs.StatusType

interface Client {
    val type : ClientType
    val user : ClientUser
    val events : SharedFlow<ClientEvent>
    suspend fun login()
    suspend fun logout()
    suspend fun restart()
    class Factory {
        private var statusType = StatusType.ONLINE
        private var clientType = ClientType.USER
        private var logLevel = LogLevel.MEDIUM
        private var token = ""

        fun setToken(token : String) : Factory {
            this.token = token
            return this
        }

        fun setClientType(clientType : ClientType) : Factory {
            this.clientType = clientType
            return this
        }

        fun setStatus(statusType : StatusType) : Factory {
            this.statusType = statusType
            return this
        }

        fun logLevel(logLevel : LogLevel) : Factory {
            this.logLevel = logLevel
            return this
        }

        fun build() : Client {
            val config = ClientConfig(
                token,
                clientType,
                statusType,
                logLevel
            )
            return ClientImpl(config).client
        }
    }

}
