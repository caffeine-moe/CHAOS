package org.caffeine.chaos.api.client

import org.caffeine.chaos.api.typedefs.ClientType
import org.caffeine.chaos.api.typedefs.StatusType

class ClientFactory {
    private var statusType = StatusType.ONLINE
    private var clientType = ClientType.USER
    private var token = ""

    fun setToken(token : String) : ClientFactory {
        this.token = token
        return this
    }

    fun setClientType(clientType : ClientType) : ClientFactory {
        this.clientType = clientType
        return this
    }

    fun setStatus(statusType : StatusType) : ClientFactory {
        this.statusType = statusType
        return this
    }

    fun build() : Client {
        val config = ClientConfig(
            token,
            clientType,
            statusType
        )
        return ClientImpl(config).client
    }
}