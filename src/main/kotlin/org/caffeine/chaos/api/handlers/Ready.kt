package org.caffeine.chaos.api.handlers

import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.client.*
import org.caffeine.chaos.api.jsonc
import org.caffeine.chaos.api.sid
import org.caffeine.chaos.api.token
import org.caffeine.chaos.log
import org.caffeine.chaos.ready

@kotlinx.serialization.Serializable
private data class ReadyPayload(
    val d: ReadyPayloadD,
    val op: Int,
    val s: Int,
    val t: String,
)

@kotlinx.serialization.Serializable
private data class ReadyPayloadD(
    val country_code: String,
    val user: ReadyPayloadDUser,
    val user_settings_proto: String,
    val v: Int,
    val session_id: String,
)

@kotlinx.serialization.Serializable
private data class ReadyPayloadDUser(
    val avatar: String?,
    val bio: String,
    val desktop: Boolean,
    val discriminator: String,
    val email: String?,
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

suspend fun ready(client: Client, payload: String) {
    val d = jsonc.decodeFromString<ReadyPayload>(payload).d
    client.user = ClientUser(
        d.user.verified,
        d.user.username,
        d.user.discriminator,
        d.user.id,
        d.user.email,
        d.user.bio,
        d.user.avatar,
        ClientRelationships(ClientFriends(client), ClientBlockedUsers(client)),
        ClientGuilds(client),
        ClientChannels(client),
        client
    )
    org.caffeine.chaos.api.ready = true
    sid = d.session_id
    token = client.config.token
    log("\u001B[38;5;47mClient logged in!", "API:")
    ready(client)
}