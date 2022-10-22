package org.caffeine.chaos.api.client.connection.handlers

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.client.ClientImpl
import org.caffeine.chaos.api.client.connection.payloads.gateway.guild.create.GuildCreateD
import org.caffeine.chaos.api.client.connection.payloads.gateway.ready.Ready
import org.caffeine.chaos.api.client.connection.payloads.gateway.ready.ReadyD
import org.caffeine.chaos.api.client.connection.payloads.gateway.ready.ReadyDPrivateChannel
import org.caffeine.chaos.api.client.connection.payloads.gateway.ready.ReadyDRelationship
import org.caffeine.chaos.api.client.user.ClientUserImpl
import org.caffeine.chaos.api.client.user.ClientUserRelationships
import org.caffeine.chaos.api.client.user.ClientUserSettings
import org.caffeine.chaos.api.json
import org.caffeine.chaos.api.models.channels.DMChannel
import org.caffeine.chaos.api.models.guild.Guild
import org.caffeine.chaos.api.models.users.BlockedUser
import org.caffeine.chaos.api.models.users.Friend
import org.caffeine.chaos.api.models.users.User
import org.caffeine.chaos.api.typedefs.ChannelType
import org.caffeine.chaos.api.utils.ConsoleColours
import org.caffeine.chaos.api.utils.log

suspend fun ready(client : ClientImpl, payload : String) {
    val d = json.decodeFromString<Ready>(payload).d
    client.userImpl = ClientUserImpl(
        d.user.verified,
        d.user.username,
        d.user.discriminator,
        d.user.id,
        d.user.email,
        d.user.bio,
        createUserSettings(d, client),
        d.user.avatar,
        d.relationships?.let { extractFriends(it, client.client) }?.let {
            ClientUserRelationships(
                it,
                extractBlockList(d.relationships, client.client)
            )
        },
        d.user.premium,
        client.configuration.token,
        d.user.bot,
        client.client,
        client
    )
    client.user = client.userImpl.user
    client.userImpl.channels.putAll(extractPrivateChannels(d.private_channels, client))
    client.userImpl.guilds.putAll(extractGuilds(d.guilds, client))
    client.utils.sessionId = d.session_id
    client.socket.ready = true
    log("${ConsoleColours.GREEN.value}Client logged in!", "API:")
    client.eventBus.produceEvent(ClientEvent.Ready(client.user))
}

private fun createUserSettings(d : ReadyD, client : ClientImpl) : ClientUserSettings? {
    if (d.user_settings == null) return null
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
        // d.user_settings.restricted_guilds,
        listOf(),
        d.user_settings.show_current_game,
        client.utils.getStatusType(d.user_settings.status),
        d.user_settings.stream_notifications_enabled,
        client.utils.getThemeType(d.user_settings.theme)
    )
}

fun extractGuilds(guilds : MutableList<GuildCreateD>, client : ClientImpl) : Map<String, Guild> {
    val map = mutableMapOf<String, Guild>()
    for (guild in guilds) {
        map[guild.id] = client.utils.createGuild(guild) ?: continue
    }
    return map
}

private fun extractFriends(relationships : MutableList<ReadyDRelationship>, client : Client) : HashMap<String, Friend> {
    val friends = hashMapOf<String, Friend>()
    for (relationship in relationships) {
        if (relationship.type == 1) {
            val friend = Friend(
                relationship.user.username,
                relationship.user.discriminator,
                relationship.user.avatar,
                relationship.user.id,
                false,
                client
            )
            friends[friend.id] = friend
        }
    }
    return friends
}

private fun extractPrivateChannels(
    channels : MutableList<ReadyDPrivateChannel>,
    client : ClientImpl,
) : HashMap<String, DMChannel> {
    val map = hashMapOf<String, DMChannel>()
    for (channel in channels) {
        val recipients = hashMapOf<String, User>()
        for (recipient in channel.recipients) {
            recipients[recipient.id] = User(
                recipient.username,
                recipient.discriminator,
                recipient.avatar,
                recipient.id,
                recipient.bot,
                client.client
            )
        }
        map[channel.id] = DMChannel(
            channel.id,
            client.client,
            ChannelType.enumById(channel.type),
            channel.last_message_id,
            channel.name.ifBlank { channel.recipients.first().username },
            recipients
        )
    }
    return map
}

private fun extractBlockList(
    relationships : MutableList<ReadyDRelationship>,
    client : Client,
) : HashMap<String, BlockedUser> {
    val blocked = hashMapOf<String, BlockedUser>()
    for (relationship in relationships) {
        if (relationship.type == 2) {
            val user = BlockedUser(
                relationship.user.username,
                relationship.user.discriminator,
                relationship.user.avatar,
                relationship.user.id,
                relationship.user.bot,
                client
            )
            blocked[user.id] = user
        }
    }
    return blocked
}
