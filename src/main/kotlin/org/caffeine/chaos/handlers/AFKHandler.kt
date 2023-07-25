package org.caffeine.chaos.handlers

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.client.user.ClientUser
import org.caffeine.chaos.api.entities.users.User
import org.caffeine.chaos.api.typedefs.ChannelType
import org.caffeine.chaos.api.utils.log
import org.caffeine.chaos.commands.oldCustomStatus
import org.caffeine.chaos.commands.oldStatus
import org.caffeine.chaos.config

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
        val i = cooldown[author] ?: return
        val cur = System.currentTimeMillis()
        if (cur.minus(i) < config.afk.cooldown * 1000) return
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