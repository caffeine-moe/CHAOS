package org.caffeine.chaos

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientUserRedeemedCodeError
import org.caffeine.chaos.api.client.ClientUserRedeemedCodeStatus
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.api.utils.log

//executed whenever a message is received
suspend fun nitroSniper(event : MessageCreateEvent, client : Client) = coroutineScope {
    //regex for a discord gift (nitro) link
    val regex = ("https://discord.gift/" + ".{16,24}".toRegex()).toRegex()
    //if the message content matches the regex (contains a nitro link) then do stuff
    if (regex.matches(event.message.content)) {
        //removes the url prefix to get the code
        val code = event.message.content.removePrefix("https://discord.gift/")
        //redeems the code then on completion does stuff
        client.user.redeemCode(code).thenAccept { rc ->
            this.launch {
                //if the nitro sniper logger is enabled then do stuff
                if (client.config.logger.nitro_sniper) {
                    //when the redeemer function returns success, print that the code was redeemed etc.
                    //when the redeemer function returns invalid and the error is that the code is unknown, say that the code was invalid.
                    when (rc.status) {
                        ClientUserRedeemedCodeStatus.SUCCESS -> {
                            log("Redeemed code ${rc.code} from ${event.message.author.discriminatedName} in ${event.channel.id}! (${rc.latency}ms)",
                                "NITRO SNIPER:")
                        }
                        ClientUserRedeemedCodeStatus.INVALID -> {
                            if (rc.error == ClientUserRedeemedCodeError.UNKNOWN_CODE) {
                                log("Code ${rc.code} from ${event.message.author.discriminatedName} in ${event.channel.id} was invalid! (${rc.latency}ms)",
                                    "NITRO SNIPER:")
                            }
                        }
                    }
                }
            }
        }
    }
}