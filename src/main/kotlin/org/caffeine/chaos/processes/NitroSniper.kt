package org.caffeine.chaos.processes

import kotlinx.coroutines.coroutineScope
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.client.user.ClientUser
import org.caffeine.chaos.api.typedefs.RedeemedCodeStatusType
import org.caffeine.chaos.api.utils.log
import org.caffeine.chaos.config

suspend fun nitroSniper(event : ClientEvent.MessageCreate, user : ClientUser) = coroutineScope {
    val regex = ("https://discord.gift/" + ".{16,24}".toRegex()).toRegex()
    if (event.message.author == user) return@coroutineScope
    if (!regex.matches(event.message.content)) return@coroutineScope
    val code = event.message.content.removePrefix("https://discord.gift/")
    user.redeemCode(code).await().also { rc ->
        if (!config.logger.nitroSniper) return@coroutineScope
        when (rc.status) {
            RedeemedCodeStatusType.SUCCESS -> {
                log(
                    "Redeemed code ${rc.code} from ${event.message.author.username} in ${event.message.channel.id}! (${rc.latency}ms)",
                    "NITRO SNIPER:"
                )
                return@coroutineScope
            }

            RedeemedCodeStatusType.INVALID -> {
                log(
                    "Code ${rc.code} from ${event.message.author.username} in ${event.message.channel.id} was invalid! (${rc.latency}ms)",
                    "NITRO SNIPER:"
                )
            }
        }
    }
}