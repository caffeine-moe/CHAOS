package org.caffeine.chaos.handlers

import org.caffeine.chaos.commands.oldCustomStatus
import org.caffeine.chaos.commands.oldStatus
import org.caffeine.chaos.config
import org.caffeine.octane.client.Client
import org.caffeine.octane.client.ClientEvent
import org.caffeine.octane.client.user.ClientUser
import org.caffeine.octane.entities.users.User
import org.caffeine.octane.typedefs.ChannelType
import org.caffeine.octane.utils.log

private val cooldown : HashMap<User, Long> = HashMap()
private val todm : MutableList<User> = mutableListOf()
private val sb = StringBuilder()

var afk = false

var afkMessage = ""

suspend fun afkHandler(event : ClientEvent.MessageCreate, client : Client) {
    if (client.user !is ClientUser) return
    val u = client.user as? ClientUser ?: return
    val author = event.message.author
    val prefix = "AFK:"
    if (author.id == client.user.id) {
        if (event.message.content.contains(afkMessage)) return
        cooldown.clear()
        afk = false
        log("Set AFK to false", prefix)
        u.setCustomStatus(oldCustomStatus)
        u.setStatus(oldStatus)
        if (todm.isEmpty()) return
        log("Users who talked to you while you were away:\n${todm.joinToString(",") { it.username }}", prefix)
        sb.clear()
    }
    if (event.message.mentionsSelf || event.channel.type != ChannelType.DM) return
    if (cooldown.contains(author)) {
        val coolDown = cooldown[author] ?: return
        val time = System.currentTimeMillis()
        if (time.minus(coolDown) < config.afk.cooldown * 1000) return
    }
    cooldown[event.message.author] = System.currentTimeMillis()
    if (!todm.contains(author)) todm.add(author)
    val message = if (event.channel.type == ChannelType.DM) {
        afkMessage
    } else {
        "$afkMessage ${author.asMention}"
    }
    event.channel.sendMessage(message).await()
}