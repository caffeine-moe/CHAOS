package org.caffeine.chaos.api.client

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.caffeine.chaos.api.client.config.ClientConfig
import org.caffeine.chaos.api.client.connection.Connection
import org.caffeine.chaos.api.client.connection.http.HTTPClient
import org.caffeine.chaos.api.client.user.BaseClientUser
import org.caffeine.chaos.api.client.user.BaseClientUserImpl
import org.caffeine.chaos.api.typedefs.ClientType
import org.caffeine.chaos.api.typedefs.ConnectionType
import org.caffeine.chaos.api.utils.DiscordUtils
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.channels.Channel as CoroutineChannel

class ClientImpl(
    override val configuration : ClientConfig,
    override val coroutineContext : CoroutineContext = Dispatchers.IO,
) : Client {

    init {
        if (configuration.clientType == ClientType.BOT && !configuration.token.startsWith("Bot ")) {
            configuration.token =
                "Bot ${configuration.token}"
        }
    }

    override val token : String = configuration.token
    val type : ClientType = configuration.clientType
    val eventBus : EventBus = EventBus()
    var socket : Connection = Connection(this)
    val utils : DiscordUtils = DiscordUtils(this)
    override val events : SharedFlow<ClientEvent> = eventBus.flow

    val httpClient : HTTPClient = HTTPClient(this)

    override suspend fun login() = socket.execute(ConnectionType.CONNECT)

    override suspend fun logout() = socket.execute(ConnectionType.DISCONNECT)

    override suspend fun restart() = socket.execute(ConnectionType.RECONNECT)

    override suspend fun destroy() = socket.execute(ConnectionType.KILL)

    override lateinit var user : BaseClientUser

    val userImpl : BaseClientUserImpl get() = user as BaseClientUserImpl
}

/*
    thanks kord!
    https://github.com/kordlib/kord
*/
inline fun <reified T : ClientEvent> Client.on(
    scope : CoroutineScope = this,
    noinline consumer : suspend T.() -> Unit,
) : Job =
    events.buffer(CoroutineChannel.UNLIMITED).filterIsInstance<T>()
        .onEach { event ->
            scope.launch { runCatching { consumer(event) } }
        }
        .launchIn(scope)