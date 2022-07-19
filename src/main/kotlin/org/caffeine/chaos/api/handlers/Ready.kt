package org.caffeine.chaos.api.handlers

import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.client.*
import org.caffeine.chaos.api.client.user.ClientUser
import org.caffeine.chaos.api.client.user.ClientUserSettings
import org.caffeine.chaos.api.jsonc
import org.caffeine.chaos.api.models.Guild
import org.caffeine.chaos.api.payloads.gateway.Ready
import org.caffeine.chaos.api.payloads.gateway.data.ready.ReadyD
import org.caffeine.chaos.api.payloads.gateway.data.ready.ReadyDGuild
import org.caffeine.chaos.api.utils.ConsoleColours
import org.caffeine.chaos.api.utils.log

suspend fun ready(client : ClientImpl, payload : String, eventBus : EventBus) {
    val d = jsonc.decodeFromString<Ready>(payload).d
    val user = ClientUser(
        d.user.verified,
        d.user.username,
        d.user.discriminator,
        d.user.id,
        d.user.email,
        d.user.bio,
        createUserSettings(d, client),
        avatar = d.user.avatar,
        //relationships = ClientUserRelationships(extractFriends(d.relationships), extractBlockList(d.relationships)),
        premium = d.user.premium,
        token = client.utils.token,
        client = client.client,
        impl = client.userImpl
    )
    client.user = user
    client.userImpl.setGuilds(extractGuilds(d.guilds))
    client.ready = true
    client.utils.sessionId = d.session_id
    log("${ConsoleColours.GREEN.value}Client logged in!", "API:")
    eventBus.produceEvent(ClientEvents.Ready(user))
}

private fun createUserSettings(d : ReadyD, client : BaseClient) : ClientUserSettings {
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

fun extractGuilds(guilds : MutableList<ReadyDGuild>) : Map<String, Guild> {
    val map = mutableMapOf<String, Guild>()
    for (guild in guilds) {
        map[guild.id] = Guild(
            guild.id,
            guild.name,
            guild.icon,
            guild.description,
            guild.splash,
            guild.discovery_splash,
            guild.features.toTypedArray(),
            guild.banner,
            guild.owner_id,
            guild.application_id,
            guild.region,
            guild.afk_channel_id,
            //guild.afk_timeout,

            vanityUrl = guild.vanity_url_code,
        )
    }
    return map
}

/*private fun extractFriends(relationships : MutableList<ClientRelationship>) : MutableList<org.caffeine.chaos.api.models.User> {
    val friends = mutableListOf<org.caffeine.chaos.api.models.User>()
    for (relationship in relationships) {
        if (relationship.type == 1) {
            val friend = org.caffeine.chaos.api.models.User(
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

private fun extractBlockList(relationships : MutableList<ClientRelationship>) : MutableList<org.caffeine.chaos.api.models.User> {
    val blocked = mutableListOf<org.caffeine.chaos.api.models.User>()
    for (relationship in relationships) {
        if (relationship.type == 2) {
            val user = org.caffeine.chaos.api.models.User(
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
