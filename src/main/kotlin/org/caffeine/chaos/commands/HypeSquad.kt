package org.caffeine.chaos.commands

import org.caffeine.octane.client.Client
import org.caffeine.octane.client.ClientEvent
import org.caffeine.octane.client.user.ClientUser
import org.caffeine.octane.typedefs.HypeSquadHouseType

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