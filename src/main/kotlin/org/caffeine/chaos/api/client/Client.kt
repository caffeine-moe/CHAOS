package org.caffeine.chaos.api.client

import org.caffeine.chaos.Config
import org.caffeine.chaos.api.BASE_URL
import org.caffeine.chaos.api.Connection
import org.caffeine.chaos.api.client.message.Message
import org.caffeine.chaos.api.httpclient
import java.util.concurrent.CompletableFuture


class Client {
    private val socket: Connection = Connection(this)
    lateinit var user: ClientUser

    suspend fun login(config: Config, client: Client){
        this.socket.login(config, client)
    }
    suspend fun logout(){
        this.socket.logout()
    }

    suspend fun sendMessage(channel: String, message: Message, config: Config): CompletableFuture<Message> {
        return org.caffeine.chaos.api.client.message.sendMessage(channel, message, config)
    }
}
