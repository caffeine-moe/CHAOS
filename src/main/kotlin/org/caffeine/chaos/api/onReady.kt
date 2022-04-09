package org.caffeine.chaos.api

import io.ktor.client.features.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.caffeine.chaos.*

@kotlinx.serialization.Serializable
data class Client(
    val d: Dee,
    val op: Int,
    val s: Int,
    val t: String
)

@kotlinx.serialization.Serializable
data class Dee(
    val country_code: String,
    val user: User,
    val user_settings: UserSettings,
    val user_settings_proto: String,
    val v: Int
)

@kotlinx.serialization.Serializable
data class User(
    val accent_color: Int,
    val avatar: String,
    val banner_color: String,
    val bio: String,
    val desktop: Boolean,
    val discriminator: String,
    val email: String,
    val flags: Int,
    val id: String,
    val mfa_enabled: Boolean,
    val mobile: Boolean,
    val nsfw_allowed: Boolean,
    val premium: Boolean,
    val purchased_flags: Int,
    val username: String,
    val verified: Boolean
)

@kotlinx.serialization.Serializable
data class UserSettings(
    val afk_timeout: Int,
    val allow_accessibility_detection: Boolean,
    val animate_emoji: Boolean,
    val animate_stickers: Int,
    val contact_sync_enabled: Boolean,
    val convert_emoticons: Boolean,
    val default_guilds_restricted: Boolean,
    val detect_platform_accounts: Boolean,
    val developer_mode: Boolean,
    val disable_games_tab: Boolean,
    val enable_tts_command: Boolean,
    val explicit_content_filter: Int,
    val friend_discovery_flags: Int,
    val gif_auto_play: Boolean,
    val inline_attachment_media: Boolean,
    val inline_embed_media: Boolean,
    val locale: String,
    val message_display_compact: Boolean,
    val native_phone_integration_enabled: Boolean,
    val passwordless: Boolean,
    val render_embeds: Boolean,
    val render_reactions: Boolean,
    val show_current_game: Boolean,
    val status: String,
    val stream_notifications_enabled: Boolean,
    val theme: String,
    val timezone_offset: Int,
    val view_nsfw_guilds: Boolean
)

suspend fun onReady(readypayload: String, config: Config, ws: DefaultClientWebSocketSession){
    val client = Json{ ignoreUnknownKeys=true
        coerceInputValues = true}.decodeFromString<Client>(readypayload)
    Log("\u001B[38;5;47mClient logged in!")
    print("\u001b[H\u001b[2J\u001B[38;5;255m")
    loginPrompt(client, config)
    configWatcher(client, ws)
}