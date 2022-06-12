package org.caffeine.chaos.api.handlers

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.*
import org.caffeine.chaos.api.client.*
import org.caffeine.chaos.log
import org.caffeine.chaos.ready

@kotlinx.serialization.Serializable
private data class ReadyPayload(
    val d : ReadyPayloadD,
    val op : Int,
    val s : Int,
    val t : String,
)

@kotlinx.serialization.Serializable
private data class ReadyPayloadD(
    val country_code : String,
    val user : ReadyPayloadDUser,
    val user_settings_proto : String,
    val v : Int,
    val session_id : String,
)

@kotlinx.serialization.Serializable
private data class ReadyPayloadDUser(
    val avatar : String?,
    val bio : String,
    val desktop : Boolean,
    val discriminator : String,
    val email : String?,
    val flags : Int,
    val id : String,
    val mfa_enabled : Boolean,
    val mobile : Boolean,
    val nsfw_allowed : Boolean,
    val premium : Boolean,
    val purchased_flags : Int,
    val username : String,
    val verified : Boolean,
)

@kotlinx.serialization.Serializable
private data class Settings(
    val afk_timeout : Int = 0,
    val allow_accessibility_detection : Boolean = false,
    val animate_emoji : Boolean = false,
    val animate_stickers : Int = 0,
    val contact_sync_enabled : Boolean = false,
    val convert_emoticons : Boolean = false,
    val custom_status : CustomStatus = CustomStatus(),
    val default_guilds_restricted : Boolean = false,
    val detect_platform_accounts : Boolean = false,
    val developer_mode : Boolean = false,
    val disable_games_tab : Boolean = false,
    val enable_tts_command : Boolean = false,
    val explicit_content_filter : Int = 0,
    val friend_discovery_flags : Int = 0,
    val gif_auto_play : Boolean = false,
    val guild_positions : List<String> = listOf(),
    val inline_attachment_media : Boolean = false,
    val inline_embed_media : Boolean = false,
    val locale : String = "",
    val message_display_compact : Boolean = false,
    val native_phone_integration_enabled : Boolean = false,
    val passwordless : Boolean = false,
    val render_embeds : Boolean = false,
    val render_reactions : Boolean = false,
    val restricted_guilds : List<String> = listOf(),
    val show_current_game : Boolean = false,
    val status : String = "",
    val stream_notifications_enabled : Boolean = false,
    val theme : String = "",
    val timezone_offset : Int = 0,
)

@kotlinx.serialization.Serializable
data class CustomStatus(
    val emoji_id : String? = "",
    val emoji_name : String? = "",
    val expires_at : String? = "",
    val text : String = "",
)

suspend fun ready(client : Client, payload : String) {
    val d = jsonc.decodeFromString<ReadyPayload>(payload).d
    val moreinforesponse = discordHTTPClient.get("$BASE_URL/users/@me/settings") {
        headers {
            append(HttpHeaders.Authorization, client.config.token)
        }
    }
    val moreinfo = jsonc.decodeFromString<Settings>(moreinforesponse.bodyAsText())
    client.user = ClientUser(
        d.user.verified,
        d.user.username,
        d.user.discriminator,
        d.user.id,
        d.user.email,
        d.user.bio,
        moreinfo.custom_status,
        moreinfo.status,
        avatar = d.user.avatar,
        relationships = ClientRelationships(ClientFriends(client), ClientBlockedUsers(client)),
        guilds = ClientGuilds(client),
        channels = ClientChannels(client),
        client = client
    )
    ready = true
    sid = d.session_id
    token = client.config.token
    log("\u001B[38;5;47mClient logged in!", "API:")
    ready(client)
}