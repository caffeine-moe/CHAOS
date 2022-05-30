package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.caffeine.chaos.Command
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.DiscordHypeSquadHouse
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent

class HypeSquad : Command(arrayOf("hs", "house", "hypesquad")) {
    override suspend fun onCalled(client: Client, event: MessageCreateEvent, args: MutableList<String>, cmd: String) =
        coroutineScope {
            if (args.isNotEmpty()) {
                var err = ""
                var house = DiscordHypeSquadHouse.NONE
                when (args.first()) {
                    "none", "0" -> house = DiscordHypeSquadHouse.NONE
                    "bravery", "1" -> house = DiscordHypeSquadHouse.BRAVERY
                    "brilliance", "2" -> house = DiscordHypeSquadHouse.BRILLIANCE
                    "balance", "3" -> house = DiscordHypeSquadHouse.BALANCE
                    else -> {
                        err = "Invalid HypeSquad house '${args.joinToString(" ")}'"
                    }
                }
                if (err.isNotBlank()) {
                    event.channel.sendMessage(
                        MessageBuilder()
                            .appendLine("**Incorrect usage** '${event.message.content}'")
                            .appendLine("**Error:** $err")
                            .appendLine("**Correct usage:** `${client.config.prefix}hypesquad HypeSquadHouse`")
                            .build())
                        .thenAccept { message -> this.launch { onComplete(message, client, true) } }
                    return@coroutineScope
                }
                client.user.setHouse(house)
                return@coroutineScope
            }
            event.channel.sendMessage(
                MessageBuilder()
                    .appendLine("**Incorrect usage** '${event.message.content}'")
                    .appendLine("**Error:** No house specified.")
                    .appendLine("**Correct usage:** `${client.config.prefix}hypesquad HypeSquadHouse`")
                    .build())
                .thenAccept { message -> this.launch { onComplete(message, client, true) } }
            return@coroutineScope
        }
}