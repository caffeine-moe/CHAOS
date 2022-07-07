package org.caffeine.chaos.api.handlers

import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.client.*
import org.caffeine.chaos.api.client.user.ClientUser
import org.caffeine.chaos.api.client.user.ClientUserSettings
import org.caffeine.chaos.api.jsonc
import org.caffeine.chaos.api.models.Guild
import org.caffeine.chaos.api.utils.ConsoleColours
import org.caffeine.chaos.api.utils.log

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
    val user_settings : ReadyPayloadDUserSettings,
    val user_settings_proto : String,
    val v : Int,
    //val relationships : MutableList<ClientRelationship>,
    //val guilds : MutableList<Guild>,
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
private data class ReadyPayloadDUserSettings(
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
private data class CustomStatus(
    val emoji_id : String? = "",
    val emoji_name : String? = "",
    val expires_at : String? = "",
    val text : String = "",
)

suspend fun ready(client : Client, payload : String, eventBus : EventBus) {
    val d = jsonc.decodeFromString<ReadyPayload>(payload).d
    client.user = ClientUser(
        d.user.verified,
        d.user.username,
        d.user.discriminator,
        d.user.id,
        d.user.email,
        d.user.bio,
        createUserSettings(d, client),
        avatar = d.user.avatar,
        //relationships = ClientRelationships(extractFriends(d.relationships), extractBlockList(d.relationships)),
        //guilds = d.guilds,
        //channels = ClientChannels(client),
        premium = d.user.premium,
        token = client.utils.token,
        client = client
    )
    client.socket.ready = true
    client.utils.sessionId = d.session_id
    log("${ConsoleColours.GREEN.value}Client logged in!", "API:")
    eventBus.produceEvent(ClientEvents.Ready)
}

private fun createUserSettings(d: ReadyPayloadD, client : Client) : ClientUserSettings {
    return ClientUserSettings(
        d.user_settings.afk_timeout,
        d.user_settings.allow_accessibility_detection,
        d.user_settings.animate_emoji,
        d.user_settings.animate_stickers,
        d.user_settings.contact_sync_enabled,
        d.user_settings.convert_emoticons,
        org.caffeine.chaos.api.client.user.CustomStatus(
            d.user_settings.custom_status.emoji_id,
            d.user_settings.custom_status.emoji_name,
            d.user_settings.custom_status.expires_at,
            d.user_settings.custom_status.text
        ),
        d.user_settings.default_guilds_restricted,
        d.user_settings.detect_platform_accounts,
        d.user_settings.developer_mode,
        d.user_settings.disable_games_tab,
        d.user_settings.enable_tts_command,
        d.user_settings.explicit_content_filter,
        d.user_settings.friend_discovery_flags,
        d.user_settings.gif_auto_play,
        listOf(),
        //d.user_settings.guild_positions,
        d.user_settings.inline_attachment_media,
        d.user_settings.inline_embed_media,
        d.user_settings.locale,
        d.user_settings.message_display_compact,
        d.user_settings.native_phone_integration_enabled,
        d.user_settings.passwordless,
        d.user_settings.render_embeds,
        d.user_settings.render_reactions,
        //d.user_settings.restricted_guilds,
        listOf(),
        d.user_settings.show_current_game,
        client.utils.getStatusType(d.user_settings.status),
        d.user_settings.stream_notifications_enabled,
        client.utils.getThemeType(d.user_settings.theme),
    )
}

/*

private fun extractFriends(relationships : MutableList<ClientRelationship>) : MutableList<ClientFriend> {
    val friends = mutableListOf<ClientFriend>()
    for (relationship in relationships) {
        if (relationship.type == 1) {
            val friend = ClientFriend(
                relationship.user.username,
                relationship.user.discriminator,
                relationship.user.id,
                relationship.user.avatar
            )
            friends.add(friend)
        }
    }
    return friends
}

private fun extractBlockList(relationships : MutableList<ClientRelationship>) : MutableList<ClientBlockedUser> {
    val blocked = mutableListOf<ClientBlockedUser>()
    for (relationship in relationships) {
        if (relationship.type == 2) {
            val user = ClientBlockedUser(
                relationship.user.username,
                relationship.user.discriminator,
                relationship.user.avatar,
                relationship.user.id
            )
            blocked.add(user)
        }
    }
    return blocked
}*/
