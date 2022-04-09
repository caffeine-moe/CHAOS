package org.caffeine.chaos

import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.caffeine.chaos.api.BASE_URL
import org.caffeine.chaos.api.httpclient
import org.caffeine.chaos.api.Client
import org.caffeine.chaos.commands.gfr
import org.caffeine.chaos.commands.ggl

private var friendcount = 0
private var guildcount = 0

fun getfriends(config: Config) {
    runBlocking {
        try {
            val response = httpclient.request<String>("https://discord.com/api/v8/users/@me/relationships") {
                method = HttpMethod.Get
                headers {
                    append(HttpHeaders.Authorization, config.token)
                }
            }
            val final = Json { ignoreUnknownKeys = true }.decodeFromString<List<gfr>>(response)
            friendcount = final.size
        } catch (e: Exception) {
            println(e)
        }
    }
}

fun getguilds(config: Config) {
    runBlocking {
        try {
            val response = httpclient.request<String>("https://discord.com/api/v8/users/@me/guilds") {
                method = HttpMethod.Get
                headers {
                    append(HttpHeaders.Authorization, config.token)
                }
            }
            val final = Json { ignoreUnknownKeys = true }.decodeFromString<List<ggl>>(response)
            guildcount = final.size
        } catch (e: Exception) {
            println(e)
        }
    }
}

fun loginPrompt(client: Client, config: Config) {
    getfriends(config)
    getguilds(config)
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
    println("\u001B[38;5;255mLogged in as: ${client.d.user.username}\u001B[38;5;33m#${client.d.user.discriminator}")
    println("\u001B[38;5;255mFriends: \u001B[38;5;33m$friendcount")
    println("\u001B[38;5;255mGuilds: \u001B[38;5;33m${guildcount}")
    println("\u001B[38;5;255m─────────────────────────────────────────────")
}