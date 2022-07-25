package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvents
import org.caffeine.chaos.api.typedefs.HypeSquadHouseType
import org.caffeine.chaos.api.utils.log


class HypeSquad :
    Command(
        arrayOf("hypesquad", "house", "hs"),
        CommandInfo("HypeSquad", "hypesquad <House>", "Changes your HypeSquad house.")
    ) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvents.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) =
        coroutineScope {
            var house = HypeSquadHouseType.NONE
            val err = if (args.isEmpty()) {
                "No house specified."
            } else {
                if (args.first().toIntOrNull() != null) {
                    if (client.utils.getHouseType(args.first().toInt()) == HypeSquadHouseType.UNKNOWN) {
                        "Invalid HypeSquad house '${args.joinToString(" ")}'"
                    } else {
                        house = client.utils.getHouseType(args.first().toInt())
                        ""
                    }
                } else {
                    if (client.utils.getHouseType(args.first()) == HypeSquadHouseType.UNKNOWN) {
                        "Invalid HypeSquad house '${args.joinToString(" ")}'"
                    } else {
                        house = client.utils.getHouseType(args.first())
                        ""
                    }
                }
            }
            if (err.isNotBlank()) {
/*                event.channel.sendMessage(error(client, event, err, commandInfo))
                    .thenAccept { message -> this.launch { onComplete(message, client, true) } }*/
                log(err)
                return@coroutineScope
            }
            client.user.setHouse(house)
        }
}