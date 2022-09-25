package org.caffeine.chaos.commands

import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvents
import org.caffeine.chaos.api.typedefs.HypeSquadHouseType


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
    ) {
        val house = HypeSquadHouseType.valueOf(args.first().toString())
        client.user.setHouse(house)
    }
}