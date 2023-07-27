package org.caffeine.chaos.processes

import org.caffeine.chaos.api.client.user.BaseClientUser
import org.caffeine.chaos.api.client.user.ClientUser
import org.caffeine.chaos.api.utils.ConsoleColour
import org.caffeine.chaos.config
import org.caffeine.chaos.utils.clear
import org.caffeine.chaos.utils.printLogo
import org.caffeine.chaos.utils.printSeparator
import org.caffeine.chaos.versionString

fun loginPrompt(user : BaseClientUser) {
    val friends = if (user is ClientUser) (user.friends.size) else 0
    val guilds = user.guilds.size
    clear()
    printLogo()
    println("${ConsoleColour.WHITE.value}Version: ${ConsoleColour.BLUE.value}$versionString")
    println("${ConsoleColour.WHITE.value}Prefix: ${ConsoleColour.BLUE.value}${config.prefix}")
    println("${ConsoleColour.WHITE.value}Logged in as: ${ConsoleColour.BLUE.value}${user.username}")
    println("${ConsoleColour.WHITE.value}Friends: ${ConsoleColour.BLUE.value}$friends")
    println("${ConsoleColour.WHITE.value}Guilds: ${ConsoleColour.BLUE.value}$guilds")
    printSeparator()
}