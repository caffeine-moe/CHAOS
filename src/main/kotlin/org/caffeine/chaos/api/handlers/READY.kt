package org.caffeine.chaos.api

import io.ktor.client.plugins.websocket.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.caffeine.chaos.Config
import org.caffeine.chaos.Log
import org.caffeine.chaos.api.client.*
import org.caffeine.chaos.configWatcher
import org.caffeine.chaos.loginPrompt

@kotlinx.serialization.Serializable
private data class payload(
    val d: d,
    val op: Int,
    val s: Int,
    val t: String,
)

@kotlinx.serialization.Serializable
private data class d(
    val country_code: String,
    val user: user,
    val user_settings_proto: String,
    val v: Int,
)

@kotlinx.serialization.Serializable
private data class user(
    val accent_color: Int,
    val avatar: String,
    val banner_color: String,
    val bio: String,
    val desktop: Boolean,
    val discriminator: Int,
    val email: String,
    val flags: Int,
    val id: String,
    val mfa_enabled: Boolean,
    val mobile: Boolean,
    val nsfw_allowed: Boolean,
    val premium: Boolean,
    val purchased_flags: Int,
    val username: String,
    val verified: Boolean,
)

suspend fun ready(client: Client, config: Config, ws: DefaultClientWebSocketSession, payload: String) {
    val d = Json { ignoreUnknownKeys = true }.decodeFromString<payload>(payload).d.user
    client.user = ClientUser(
        d.verified,
        d.username,
        d.discriminator,
        d.id,
        d.email,
        d.bio,
        d.avatar,
        ClientFriends(),
        ClientGuilds(),
        ClientChannels()
    )
    Log("\u001B[38;5;47mClient logged in!")
    print("\u001b[H\u001b[2J\u001B[38;5;255m")
    loginPrompt(client, config)
    configWatcher(client)
}