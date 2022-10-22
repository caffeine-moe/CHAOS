package org.caffeine.chaos

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.utils.ConsoleColours
import org.caffeine.chaos.api.utils.clear
import org.caffeine.chaos.api.utils.printLogo
import org.caffeine.chaos.api.utils.printSeparator

// executed whenever the client is logged in successfully
fun loginPrompt(client : Client) {
    val friends = client.user.relationships?.friends?.size
    val guilds = client.user.guilds.size
    clear()
    printLogo()
    println("${ConsoleColours.WHITE.value}Version: ${ConsoleColours.BLUE.value}$versionString")
    println("${ConsoleColours.WHITE.value}Prefix: ${ConsoleColours.BLUE.value}${config.prefix}")
    println("${ConsoleColours.WHITE.value}Logged in as: ${client.user.username}${ConsoleColours.BLUE.value}#${client.user.discriminator}")
    println("${ConsoleColours.WHITE.value}Friends: ${ConsoleColours.BLUE.value}$friends")
    println("${ConsoleColours.WHITE.value}Guilds: ${ConsoleColours.BLUE.value}$guilds")
    printSeparator()
}
