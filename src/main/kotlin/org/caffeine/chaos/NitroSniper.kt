package org.caffeine.chaos

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientUserRedeemedCodeError
import org.caffeine.chaos.api.client.ClientUserRedeemedCodeStatus
import org.caffeine.chaos.api.client.message.MessageCreateEvent

suspend fun nitroSniper(event: MessageCreateEvent, client: Client) {
    val rg = ("https://discord.gift/" + ".{16,24}".toRegex()).toRegex()
    if (rg.matches(event.message.content)) {
        val code = event.message.content.removePrefix("https://discord.gift/")
        client.user.redeemCode(code).thenAccept { rc ->
            if (client.config.logger.nitro_sniper) {
                when (rc.status) {
                    ClientUserRedeemedCodeStatus.SUCCESS -> {
                        log("Redeemed code ${rc.code}! in ${rc.latency}ms", "NITRO SNIPER:")
                    }
                    ClientUserRedeemedCodeStatus.INVALID -> {
                        if (rc.error == ClientUserRedeemedCodeError.UNKNOWN_CODE) {
                            log("Code ${rc.code} was invalid! (${rc.latency}ms)", "NITRO SNIPER:")
                        }
                    }
                }
            }
        }
    }
}
