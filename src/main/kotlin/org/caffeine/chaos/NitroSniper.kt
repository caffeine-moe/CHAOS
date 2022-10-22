package org.caffeine.chaos

import kotlinx.coroutines.coroutineScope
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.typedefs.RedeemedCodeErrorType
import org.caffeine.chaos.api.typedefs.RedeemedCodeStatusType
import org.caffeine.chaos.api.utils.log

suspend fun nitroSniper(event : ClientEvent.MessageCreate, client : Client) = coroutineScope {
    val regex = ("https://discord.gift/" + ".{16,24}".toRegex()).toRegex()
    if (!regex.matches(event.message.content)) return@coroutineScope
    val code = event.message.content.removePrefix("https://discord.gift/")
    client.user.redeemCode(code).await().also { rc ->
        if (config.logger.nitro_sniper) {
            when (rc.status) {
                RedeemedCodeStatusType.SUCCESS -> {
                    log(
                        "Redeemed code ${rc.code} from ${event.message.author.discriminatedName} in ${event.message.channel.id}! (${rc.latency}ms)",
                        "NITRO SNIPER:"
                    )
                    return@coroutineScope
                }
                 else -> {
                     if (rc.error == RedeemedCodeErrorType.UNKNOWN_CODE) {
                         log(
                             "Code ${rc.code} from ${event.message.author.discriminatedName} in ${event.message.channel.id} was invalid! (${rc.latency}ms)",
                             "NITRO SNIPER:"
                         )
                     }
                 }
            }
        }
    }
}
