package org.caffeine.chaos

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.DiscordChannelType
import org.caffeine.chaos.api.client.DiscordUser
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.commands.oldCustomStatus
import org.caffeine.chaos.commands.oldStatus

val cooldown : HashMap<DiscordUser, Long> = HashMap()
val todm : MutableList<DiscordUser> = mutableListOf()

var afk = false

var afkMessage = ""

suspend fun AFKHandler(event : MessageCreateEvent, client : Client) {
    val author = event.message.author
    val prefix = "AFK:"
    if (author.id == client.user.id) {
        if (event.message.content.startsWith(afkMessage)) {
            return
        }
        cooldown.clear()
        afk = false
        log("Set AFK to $afk", prefix)
        client.user.setCustomStatus(oldCustomStatus)
        client.user.setStatus(oldStatus)
        if (todm.isNotEmpty()) {
            val sb = StringBuilder()
            for (i in todm) {
                sb.appendLine("${i.discriminatedName} : ${i.id}")
            }
            log("Users who talked to you while you were away:\n$sb", prefix)
        }
        return
    }
    var doit = false
    for (mention in event.message.mentions) {
        if (mention.id == client.user.id) {
            doit = true
            break
        }
    }
    if (event.channel.type() == DiscordChannelType.DM) {
        doit = true
    }
    if (doit) {
        if (cooldown.contains(author)) {
            val i = cooldown[author] ?: return
            val cur = System.currentTimeMillis()
            if (cur.minus(i) < client.config.afk.cooldown * 1000) {
                return
            }
            cooldown.remove(author)
        }
        cooldown[event.message.author] = System.currentTimeMillis()
        if (!todm.contains(author)) {
            todm.add(author)
        }
        event.channel.sendMessage(MessageBuilder()
            .appendLine("$afkMessage <@${author.id}>")
            .build()
        )
    }
}