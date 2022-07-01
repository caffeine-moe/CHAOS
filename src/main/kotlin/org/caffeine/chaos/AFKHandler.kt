package org.caffeine.chaos

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.DiscordChannelType
import org.caffeine.chaos.api.client.DiscordUser
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.api.utils.log
import org.caffeine.chaos.commands.oldCustomStatus
import org.caffeine.chaos.commands.oldStatus

private val cooldown : HashMap<DiscordUser, Long> = HashMap()
private val todm : MutableList<DiscordUser> = mutableListOf()
private val sb = StringBuilder()

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
            for (i in todm) {
                sb.appendLine("${i.discriminatedName} : ${i.id}")
            }
            log("Users who talked to you while you were away:\n${sb.trimEnd()}", prefix)
            sb.clear()
        }
        return
    }
    var doit = false
    if (event.message.mentions.any {
            it.id == client.user.id
        }) {
        doit = true
    }
/*    if (event.channel.type() == DiscordChannelType.DM) {
        doit = true
    }*/
    if (doit) {
        if (cooldown.contains(author)) {
            val i = cooldown[author] ?: return
            val cur = System.currentTimeMillis()
            if (cur.minus(i) < config.afk.cooldown * 1000) {
                return
            }
            cooldown.remove(author)
        }
        cooldown[event.message.author] = System.currentTimeMillis()
        if (!todm.contains(author)) {
            todm.add(author)
        }
/*
        val message = if (event.channel.type() == DiscordChannelType.DM) {
*/
            afkMessage
        } else {
            "$afkMessage <@${author.id}>"
        }
/*        event.channel.sendMessage(MessageBuilder()
            .appendLine(message)
            .build()
        )*/
    }