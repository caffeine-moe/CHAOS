package org.caffeine.chaos.commands

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.client.user.ClientUser
import org.caffeine.chaos.api.typedefs.HypeSquadHouseType

class HypeSquad :
    Command(
        arrayOf("hypesquad", "house", "hs"),
        CommandInfo("HypeSquad", "hypesquad <House>", "Changes your HypeSquad house.")
    ) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : List<String>,
        cmd : String,
    ) {
        if (client.user !is ClientUser) return
        val house = HypeSquadHouseType.enumById(args.first())
        (client.user as ClientUser).setHouse(house)
    }
}