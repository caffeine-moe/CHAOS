package org.caffeine.chaos

import org.caffeine.chaos.api.client.user.ClientUser
import org.caffeine.chaos.api.utils.ConsoleColour
import org.caffeine.chaos.api.utils.clear
import org.caffeine.chaos.api.utils.printLogo
import org.caffeine.chaos.api.utils.printSeparator

fun loginPrompt(user : ClientUser) {
    val friends = user.friends.size
    val guilds = user.guilds.size
    clear()
    printLogo()
    println("${ConsoleColour.WHITE.value}Version: ${ConsoleColour.BLUE.value}$versionString")
    println("${ConsoleColour.WHITE.value}Prefix: ${ConsoleColour.BLUE.value}${config.prefix}")
    println("${ConsoleColour.WHITE.value}Logged in as: ${user.username}${ConsoleColour.BLUE.value}#${user.discriminator}")
    println("${ConsoleColour.WHITE.value}Friends: ${ConsoleColour.BLUE.value}$friends")
    println("${ConsoleColour.WHITE.value}Guilds: ${ConsoleColour.BLUE.value}$guilds")
    printSeparator()
}
