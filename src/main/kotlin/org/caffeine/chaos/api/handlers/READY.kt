package org.caffeine.chaos.api

import io.ktor.client.plugins.websocket.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.caffeine.chaos.*
import org.caffeine.chaos.api.client.*

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
    val session_id: String
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

suspend fun ready(client: Client, config: Config, connection: Connection, payload: String) {
    val d = Json { ignoreUnknownKeys = true }.decodeFromString<payload>(payload).d
    client.user = ClientUser(
        d.user.verified,
        d.user.username,
        d.user.discriminator,
        d.user.id,
        d.user.email,
        d.user.bio,
        d.user.avatar,
        ClientFriends(),
        ClientGuilds(),
        ClientChannels()
    )
    connection.sid = d.session_id
    log("\u001B[38;5;47mClient logged in!")
    clear()
    loginPrompt(client, config)
    configWatcher(client)
}