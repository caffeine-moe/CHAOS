package org.caffeine.chaos.api.client

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.SharedFlow
import org.caffeine.chaos.api.client.connection.Connection
import org.caffeine.chaos.api.typedefs.ConnectionType
import org.caffeine.chaos.api.client.connection.http.HTTPClient
import org.caffeine.chaos.api.client.user.ClientUser
import org.caffeine.chaos.api.client.user.ClientUserImpl
import org.caffeine.chaos.api.typedefs.ClientType
import org.caffeine.chaos.api.typedefs.LogLevel
import org.caffeine.chaos.api.typedefs.LoggerLevel
import org.caffeine.chaos.api.typedefs.StatusType
import org.caffeine.chaos.api.utils.DiscordUtils
import kotlin.coroutines.CoroutineContext

class ClientImpl(
    override val token : String,
    override val clientType : ClientType,
    override val statusType : StatusType,
    override val logLevelLevel : LoggerLevel
) : Client {

    override val logLevel : LogLevel = LogLevel(logLevelLevel, this)
    val configuration : ClientConfig = this
    override val type : ClientType = configuration.clientType
    val eventBus : EventBus = EventBus()
    val socket : Connection = Connection(this)
    val utils : DiscordUtils = DiscordUtils().apply { client = this@ClientImpl }
    override val events : SharedFlow<ClientEvent> = eventBus.flow

    val httpClient : HTTPClient = HTTPClient.build(this)

    override suspend fun login() = socket.execute(ConnectionType.CONNECT)
    override suspend fun logout() = socket.execute(ConnectionType.DISCONNECT)
    override suspend fun restart() = socket.execute(ConnectionType.RECONNECT)


    lateinit var userImpl : ClientUserImpl
    override lateinit var user : ClientUser
}
