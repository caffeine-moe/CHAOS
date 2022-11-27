package org.caffeine.chaos

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.entities.users.User
import org.caffeine.chaos.api.typedefs.ChannelType
import org.caffeine.chaos.api.utils.log
import org.caffeine.chaos.api.utils.log
import org.caffeine.chaos.commands.oldCustomStatus
import org.caffeine.chaos.commands.oldStatus

private val cooldown : HashMap<User, Long> = HashMap()
private val todm : MutableList<User> = mutableListOf()
private val sb = StringBuilder()

var afk = false

var afkMessage = ""

suspend fun afkHandler(event : ClientEvent.MessageCreate, client : Client) {
    val author = event.message.author
    val prefix = "AFK:"
    if (author.id == client.user.id) {
        if (event.message.content.contains(afkMessage)) return
        cooldown.clear()
        afk = false
        log("Set AFK to $afk", prefix)
        client.user.setCustomStatus(oldCustomStatus)
        client.user.setStatus(oldStatus)
        if (todm.isNotEmpty()) {
            log("Users who talked to you while you were away:\n${todm.toString().trimEnd()}", prefix)
            sb.clear()
        }
        return
    }
    var doit = false
    /*    if (event.message.mentions.any {
                it.id == client.autoDeleteUser.id
            }) {
            doit = true
        }*/
    if (event.channel.type == ChannelType.DM) {
        doit = true
    }
    if (!doit) return
    if (cooldown.contains(author)) {
        val i = cooldown[author] ?: return
        val cur = System.currentTimeMillis()
        if (cur.minus(i) < config.afk.cooldown * 1000) {
            return
        }
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
