package org.caffeine.chaos

import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.caffeine.chaos.api.httpclient
import org.caffeine.chaos.api.client.Client

suspend fun loginPrompt(client: Client, config: Config) {
    val friends = client.user.friends.getAmount(config)
    val guilds = client.user.guilds.getAmount(config)
    println(
                " ▄████▄   ██░ ██  ▄▄▄       ▒█████    ██████ \n" +
                "▒██▀ ▀█  ▓██░ ██▒▒████▄    ▒██▒  ██▒▒██    ▒ \n" +
                "▒▓█    ▄ ▒██▀▀██░▒██  ▀█▄  ▒██░  ██▒░ ▓██▄   \n" +
                "▒▓▓▄ ▄██▒░▓█ ░██ ░██▄▄▄▄██ ▒██   ██░  ▒   ██▒\n" +
                "▒ ▓███▀ ░░▓█▒░██▓ ▓█   ▓██▒░ ████▓▒░▒██████▒▒\n" +
                "░ ░▒ ▒  ░ ▒ ░░▒░▒ ▒▒   ▓▒█░░ ▒░▒░▒░ ▒ ▒▓▒ ▒ ░\n" +
                "  ░  ▒    ▒ ░▒░ ░  ▒   ▒▒ ░  ░ ▒ ▒░ ░ ░▒  ░ ░\n" +
                "░         ░  ░░ ░  ░   ▒   ░ ░ ░ ▒  ░  ░  ░  \n" +
                "░ ░       ░  ░  ░      ░  ░    ░ ░        ░  \n" +
                "░                                            "
    )
    println("\u001B[38;5;255mVersion: \u001B[38;5;33m${version}")
    println("\u001B[38;5;255mPrefix: \u001B[38;5;33m${config.prefix}")
    println("\u001B[38;5;255mLogged in as: ${client.user.username}\u001B[38;5;33m#${client.user.discriminator}")
    println("\u001B[38;5;255mFriends: \u001B[38;5;33m${friends}")
    println("\u001B[38;5;255mGuilds: \u001B[38;5;33m${guilds}")
    println("\u001B[38;5;255m─────────────────────────────────────────────")
}