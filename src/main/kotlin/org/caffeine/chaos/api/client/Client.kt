package org.caffeine.chaos.api.client

import org.caffeine.chaos.api.Connection
import org.caffeine.chaos.api.ConnectionType
import org.caffeine.chaos.api.client.user.ClientUser
import org.caffeine.chaos.api.utils.DiscordUtils
import java.util.concurrent.CompletableFuture

interface ClientEvents {
    val ready: () -> Unit
}

private interface ClientInternal {
    var user: ClientUser
    //val guilds: HashMap<string, Guild>
    //val channels: HashMap<string, BaseChannel>
    //val relationships: HashMap<string, ClientRelationship>
    var socket: Connection
    var rest: DiscordUtils
    suspend fun login(token: String)
    suspend fun logout()
}

class Client() : ClientInternal {
    override var socket : Connection = Connection()
    override var rest : DiscordUtils = DiscordUtils()
    override var user : ClientUser
        get() = TODO("Not yet implemented")
        set(value) {
            TODO("Not yet implemented")
        }

    override suspend fun login(token: String) {
        rest.token = token
        socket.execute(ConnectionType.CONNECT, this)
    }

    override suspend fun logout() {
        socket.execute(ConnectionType.DISCONNECT, this)
    }

}



/*
class Client(
    var config : Config,
    val socket : Connection = Connection(),
) {
    lateinit var user : ClientUser
    suspend fun login() {
        client = this
        socket.execute(ConnectionType.CONNECT, this)
    }

    suspend fun logout() {
        socket.execute(ConnectionType.DISCONNECT, this)
    }
}*/
