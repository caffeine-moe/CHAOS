package org.caffeine.chaos.commands

import org.caffeine.chaos.Config
import org.caffeine.chaos.api.deleteMessage
import org.caffeine.chaos.api.messageCreate
import org.caffeine.chaos.api.sendMessageResponse
import org.javacord.api.entity.message.Message
import org.javacord.api.event.message.MessageCreateEvent
import kotlin.concurrent.thread


suspend fun user(event: messageCreate, config: Config) {
    Thread.sleep(config.auto_delete.user_delay * 1000)
    val message = event.d.id
    val channel = event.d.channel_id
    deleteMessage(channel.toString(), message.toString(), config)
}

suspend fun bot(msg: sendMessageResponse, config: Config) {
    if (config.auto_delete.bot.enabled) {
        Thread.sleep(config.auto_delete.bot_delay * 1000)
        deleteMessage(msg.channel_id, msg.content, config)
    }
}
