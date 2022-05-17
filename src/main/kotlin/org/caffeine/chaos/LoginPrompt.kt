package org.caffeine.chaos

import org.caffeine.chaos.api.client.Client

suspend fun loginPrompt(client: Client) {
    val friends = client.user.relationships.friends.getAmount()
    val guilds = client.user.guilds.getAmount()
    clear()
    printLogo()
    println("\u001B[38;5;255mVersion: \u001B[38;5;33m${version}")
    println("\u001B[38;5;255mPrefix: \u001B[38;5;33m${client.config.prefix}")
    println("\u001B[38;5;255mLogged in as: ${client.user.username}\u001B[38;5;33m#${client.user.discriminator}")
    println("\u001B[38;5;255mFriends: \u001B[38;5;33m$friends")
    println("\u001B[38;5;255mGuilds: \u001B[38;5;33m$guilds")
    printSeparator()
}