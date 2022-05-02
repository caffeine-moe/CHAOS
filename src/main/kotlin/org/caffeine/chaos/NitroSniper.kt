package org.caffeine.chaos

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientUserRedeemedCodeError
import org.caffeine.chaos.api.client.ClientUserRedeemedCodeStatus
import org.caffeine.chaos.api.client.message.MessageCreateEvent

suspend fun nitroSniper(event: MessageCreateEvent, client: Client) {
    val giftPrefix = "discord.gift/"
    val rg = giftPrefix + ".{16,24}".toRegex()
    val match = rg.toRegex().matches(event.message.content)
    if (match) {
        val code = event.message.content.removePrefix(giftPrefix)
        client.user.redeemCode(code).thenAccept { rc ->
            if (!client.config.nitro_sniper.silent) {
                when (rc.status) {
                    ClientUserRedeemedCodeStatus.SUCCESS -> {
                        log("Redeemed code ${rc.code}! in ${rc.latency}ms", "NITRO SNIPER:")
                    }
                    ClientUserRedeemedCodeStatus.INVALID -> {
                        when (rc.error) {
                            ClientUserRedeemedCodeError.UNKNOWN_CODE -> {
                                log("Code ${rc.code} was invalid! (${rc.latency}ms)", "NITRO SNIPER:")
                            }
                            else -> {}
                        }
                    }
                }
            }
        }
    }
}
