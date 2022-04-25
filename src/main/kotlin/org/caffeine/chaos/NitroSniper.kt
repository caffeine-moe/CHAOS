package org.caffeine.chaos

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageCreateEvent

suspend fun nitroSniper(event: MessageCreateEvent, client: Client) {
    val giftPrefix = "discord.gift/"
    val rg = giftPrefix + ".{16,24}".toRegex()
    val match = rg.toRegex().matches(event.message.content)
    if (match) {
        val code = event.message.content.removePrefix(giftPrefix)
        val start = System.currentTimeMillis()
        try {
            client.user.redeemCode(code)
            val end = System.currentTimeMillis()
            val time = start - end
            if (!client.config.nitro_sniper.silent) {
                log("Redeemed code $code! in ${time.toString().replace("-", "")}ms", "NITRO SNIPER:")
            }
        } catch (e: Exception) {
            if (e.toString().contains("Unknown Gift Code")) {
                if (!client.config.nitro_sniper.silent) {
                    log("Code $code is invalid!", "NITRO SNIPER:")
                }
            }
            if (e.toString().contains("e-mail")) {
                if (!client.config.nitro_sniper.silent) {
                    log("You need to verify your email in order to use the nitro sniper! (Code: $code)",
                        "NITRO SNIPER:")
                }
            }
        }
    }
}
