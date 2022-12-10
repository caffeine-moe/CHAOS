package org.caffeine.chaos.api.client

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.caffeine.chaos.api.client.connection.Connection
import org.caffeine.chaos.api.client.connection.http.HTTPClient
import org.caffeine.chaos.api.client.user.ClientUser
import org.caffeine.chaos.api.client.user.ClientUserImpl
import org.caffeine.chaos.api.typedefs.*
import org.caffeine.chaos.api.utils.DiscordUtils
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.channels.Channel as CoroutineChannel

class ClientImpl(
    override val token : String,
    override val clientType : ClientType,
    override val statusType : StatusType,
    override val loggerLevel : LoggerLevel,
    override val coroutineContext : CoroutineContext = Dispatchers.IO,
) : Client {

    override val logLevel : LogLevel = LogLevel(loggerLevel, this)
    val configuration : ClientConfig = this
    override val type : ClientType = configuration.clientType
    val eventBus : EventBus = EventBus()
    val socket : Connection = Connection(this)
    val utils : DiscordUtils = DiscordUtils(this)
    override val events : SharedFlow<ClientEvent> = eventBus.flow

    val httpClient : HTTPClient = HTTPClient.build(this)

    override suspend fun login() = socket.execute(ConnectionType.CONNECT)
    override suspend fun logout() = socket.execute(ConnectionType.DISCONNECT)
    override suspend fun restart() = socket.execute(ConnectionType.RECONNECT)


    lateinit var userImpl : ClientUserImpl
    override lateinit var user : ClientUser
}

/*
    thanks kord!
    https://github.com/kordlib/kord
*/
inline fun <reified T : ClientEvent> Client.on(
    scope: CoroutineScope = this,
    noinline consumer: suspend T.() -> Unit
): Job =
    events.buffer(CoroutineChannel.UNLIMITED).filterIsInstance<T>()
        .onEach { event ->
            scope.launch { runCatching { consumer(event) } }
        }
        .launchIn(scope)