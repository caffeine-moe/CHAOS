package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.DiscordHypeSquadHouse
import org.caffeine.chaos.api.client.message.MessageCreateEvent

class HypeSquad :
    Command(arrayOf("hypesquad", "house", "hs"),
        CommandInfo("HypeSquad", "hypesquad <House>", "Changes your HypeSquad house.")) {
    override suspend fun onCalled(
        client : Client,
        event : MessageCreateEvent,
        args : MutableList<String>,
        cmd : String,
    ) =
        coroutineScope {
            var house = DiscordHypeSquadHouse.NONE
            var err = ""
            if (args.isEmpty()) {
                err = "No house specified."
            } else {
                when (args.first().lowercase()) {
                    "none", "0" -> house = DiscordHypeSquadHouse.NONE
                    "bravery", "1" -> house = DiscordHypeSquadHouse.BRAVERY
                    "brilliance", "2" -> house = DiscordHypeSquadHouse.BRILLIANCE
                    "balance", "3" -> house = DiscordHypeSquadHouse.BALANCE
                    else -> {
                        err = "Invalid HypeSquad house '${args.joinToString(" ")}'"
                    }
                }
            }
            if (err.isNotBlank()) {
                event.channel.sendMessage(error(client, event, err, commandInfo))
                    .thenAccept { message -> this.launch { onComplete(message, client, true) } }
                return@coroutineScope
            }
            client.user.setHouse(house)
        }
}