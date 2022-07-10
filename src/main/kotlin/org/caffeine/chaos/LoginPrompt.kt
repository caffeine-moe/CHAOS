package org.caffeine.chaos

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.utils.ConsoleColours
import org.caffeine.chaos.api.utils.clear
import org.caffeine.chaos.api.utils.printLogo
import org.caffeine.chaos.api.utils.printSeparator


//executed whenever the client is logged in successfully
fun loginPrompt(client : Client) {
    //gets friend and guild count
    val friends = 0
    val guilds = client.user.guilds.size
    //clears and prints edgy hackerman logo
    clear()
    printLogo()
    //prints version number
    println("${ConsoleColours.WHITE.value}Version: ${ConsoleColours.BLUE.value}${versionString}")
    //prints the prefix
    println("${ConsoleColours.WHITE.value}Prefix: ${ConsoleColours.BLUE.value}${config.prefix}")
    //prints the name and discriminator of the currently logged-in user
    println("${ConsoleColours.WHITE.value}Logged in as: ${client.user.username}${ConsoleColours.BLUE.value}#${client.user.discriminator}")
    //prints friend and guild count
    println("${ConsoleColours.WHITE.value}Friends: ${ConsoleColours.BLUE.value}$friends")
    println("${ConsoleColours.WHITE.value}Guilds: ${ConsoleColours.BLUE.value}$guilds")
    //prints line separator lol
    printSeparator()
}