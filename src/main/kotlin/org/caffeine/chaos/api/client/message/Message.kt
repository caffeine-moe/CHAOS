package org.caffeine.chaos.api.client.message

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.caffeine.chaos.Config
import java.util.concurrent.CompletableFuture

class Message(var id: String?, var author: MessageAuthor?, var content: String, var channel: MessageChannel) {
    //val mentionedUsers: String
    suspend fun edit(message: Message, config: Config) : CompletableFuture<Message>{
        return editMessage(this, config, message)
    }

    suspend fun delete(message: Message, config: Config){
        try{
        deleteMessage(message, config)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}