package org.caffeine.chaos

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.caffeine.chaos.api.client.Client
import kotlin.system.exitProcess


//executed whenever the client is logged in successfully
suspend fun loginPrompt(client: Client) = coroutineScope {
    //gets friend and guild count
    val friends = client.user.relationships.friends.getAmount()
    val guilds = client.user.guilds.getAmount()
    //clears and prints edgy hackerman logo
    clear()
    printLogo()
    //prints version number
    println("\u001B[38;5;255mVersion: \u001B[38;5;33m${versionString}")
    //prints the prefix
    println("\u001B[38;5;255mPrefix: \u001B[38;5;33m${client.config.prefix}")
    //prints the name and discriminator of the currently logged-in user
    println("\u001B[38;5;255mLogged in as: ${client.user.username}\u001B[38;5;33m#${client.user.discriminator}")
    //prints friend and guild count
    println("\u001B[38;5;255mFriends: \u001B[38;5;33m$friends")
    println("\u001B[38;5;255mGuilds: \u001B[38;5;33m$guilds")
    //prints line separator lol
    printSeparator()
    //checks if client is up to date
    if (client.config.updater.enabled) {
        val updateStatus = updateStatus()
        if (updateStatus.clientIsOutOfDate) {
            if (client.config.updater.notify) {
                log("Client is out of date!", "UPDATER:")
                log("You are on version $versionString", "UPDATER:")
                log("Latest version is ${updateStatus.latestVerString}", "UPDATER:")
                if (!client.config.updater.auto_download) {
                    log("Please update here: ${updateStatus.downUrl}", "UPDATER:")
                }
            }
            if (client.config.updater.auto_download) {
                downloadUpdate(updateStatus.downUrl).thenAccept {
                    this.launch {
                        log("Downloaded latest update to ${it}!", "UPDATER:")
                        if (client.config.updater.exit) {
                            client.logout()
                            exitProcess(69)
                        }
                    }
                }
            }
        }
    }
}