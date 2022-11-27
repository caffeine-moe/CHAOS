package org.caffeine.chaos.api.utils

import SerialGuild
import arrow.core.Invalid
import arrow.core.Valid
import arrow.core.Validated
import arrow.core.left
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import org.caffeine.chaos.api.*
import org.caffeine.chaos.api.client.ClientImpl
import org.caffeine.chaos.api.client.connection.payloads.gateway.SerialAttachment
import org.caffeine.chaos.api.client.connection.payloads.gateway.SerialMessage
import org.caffeine.chaos.api.client.connection.payloads.gateway.SerialUser
import org.caffeine.chaos.api.client.connection.payloads.gateway.guild.create.GuildCreateDChannel
import org.caffeine.chaos.api.client.connection.payloads.gateway.ready.ReadyD
import org.caffeine.chaos.api.client.connection.payloads.gateway.ready.ReadyDCustomStatus
import org.caffeine.chaos.api.client.connection.payloads.gateway.ready.ReadyDPrivateChannel
import org.caffeine.chaos.api.client.connection.payloads.gateway.ready.ReadyDUserSettings
import org.caffeine.chaos.api.client.user.ClientUserImpl
import org.caffeine.chaos.api.client.user.ClientUserSettings
import org.caffeine.chaos.api.client.user.CustomStatus
import org.caffeine.chaos.api.entities.channels.*
import org.caffeine.chaos.api.entities.guild.Guild
import org.caffeine.chaos.api.entities.guild.GuildImpl
import org.caffeine.chaos.api.entities.message.*
import org.caffeine.chaos.api.entities.users.User
import org.caffeine.chaos.api.entities.users.UserImpl
import org.caffeine.chaos.api.typedefs.*
import java.text.SimpleDateFormat
import java.util.*

class DiscordUtils {

    lateinit var client : ClientImpl

    @Serializable
    data class SuperProperties(
        var os : String = "",
        var browser : String = "",
        var device : String = "",
        var system_locale : String = "",
        var browser_user_agent : String = "",
        var browser_version : String = "",
        var os_version : String = "",
        var referrer : String = "",
        var referring_domain : String = "",
        var referrer_current : String = "",
        var referring_domain_current : String = "",
        var release_channel : String = "",
        var client_build_number : Int = 0,
        var client_event_source : String? = null,
    )

    var gatewaySequence = 0

    var sessionId = ""

    suspend fun tokenValidator(token : String) {
        val headers = HeadersBuilder().also {
            it.append("Authorization", token)
        }
        client.httpClient.get("$BASE_URL/users/@me", headers)
    }

    fun getStatusType(type : String) : StatusType {
        StatusType.values().forEach {
            if (it.value == type.lowercase()) {
                return it
            }
        }
        return StatusType.UNKNOWN
    }

    fun getHouseType(type : String) : HypeSquadHouseType {
        HypeSquadHouseType.values().forEach {
            if (it.value == type.lowercase()) {
                return it
            }
        }
        return HypeSquadHouseType.UNKNOWN
    }

    fun getHouseType(type : Number) : HypeSquadHouseType {
        HypeSquadHouseType.values().forEach {
            if (it.ordinal == type) {
                return it
            }
        }
        return HypeSquadHouseType.UNKNOWN
    }

    fun getThemeType(theme : String) : ThemeType {
        ThemeType.values().forEach {
            if (it.value == theme.lowercase()) {
                return it
            }
        }
        return ThemeType.UNKNOWN
    }

    suspend fun fetchMessages(channel : TextBasedChannel, filters : MessageFilters) : List<Message> {
        val collection : MutableList<Message> = arrayListOf()
        try {
            if (filters.authorId == client.user.id && filters.beforeId.asString().isBlank()) {
                fetchLastMessageInChannel(channel, client.user, MessageSearchFilters())?.let {
                    collection.add(it)
                    filters.beforeId = it.id
                }
            }

            while (true) {
                var parameters = ""
                if (filters.beforeId.asString().isNotBlank()) parameters += "before=${filters.beforeId.asString()}&"
                if (filters.afterId.asString().isNotBlank()) parameters += "after=${filters.afterId.asString()}&"
                if (filters.authorId.asString()
                        .isNotBlank()
                ) parameters += "author_id=${filters.authorId.asString()}&"
                if (filters.mentioningUserId.asString()
                        .isNotBlank()
                ) parameters += "mentions=${filters.mentioningUserId.asString()}&"
                val response =
                    client.httpClient.get("$BASE_URL/channels/${channel.id.asString()}/messages?$parameters").await()
                val newMessages = json.decodeFromString<MutableList<SerialMessage>>(response)
                newMessages.removeIf {
                    filters.authorId.asString().isNotBlank() && it.author.id != filters.authorId.asString()
                }
                if (newMessages.size == 0) {
                    val lastmessage = fetchLastMessageInChannel(
                        channel,
                        client.user,
                        MessageSearchFilters(before_id = filters.beforeId)
                    )
                        ?: return collection
                    collection += lastmessage
                } else {
                    newMessages.forEach {
                        when (val result = createMessage(it)) {
                            is Invalid -> {
                                log(
                                    "Error in messageCreate: ${result.left()}",
                                    level = LogLevel(LoggerLevel.LOW, client)
                                )
                            }

                            is Valid -> {
                                collection += result.value
                            }
                        }
                    }
                }

                filters.beforeId = collection.last().id

                if (filters.needed != 0 && collection.size >= filters.needed) {
                    break
                }

                delay(500)
            }
        } catch (e : Exception) {
            log("Error: ${e.message}", "API:", LogLevel(LoggerLevel.LOW, client))
            e.printStackTrace()
        }
        return if (collection.size <= filters.needed) {
            collection
        } else {
            collection.take(filters.needed)
        }
    }

    fun createGuild(payload : SerialGuild) : Guild {
        return GuildImpl(
            payload.id.asSnowflake(),
            payload.name,
            payload.icon,
            payload.description,
            payload.splash,
            payload.discovery_splash,
            payload.banner,
            payload.owner_id.asSnowflake(),
            payload.application_id?.asSnowflake(),
            payload.region,
            payload.afk_channel_id.asSnowflake(),
            payload.afk_timeout,
            payload.system_channel_id?.asSnowflake(),
            payload.widget_enabled,
            createSnowflake(""),
            payload.verification_level,
            payload.default_message_notifications,
            payload.mfa_level,
            payload.explicit_content_filter,
            0,
            payload.max_members,
            payload.max_video_channel_users,
            payload.vanity_url_code,
            payload.premium_tier,
            payload.premium_subscription_count,
            payload.system_channel_flags,
            payload.preferred_locale,
            payload.rules_channel_id?.asSnowflake(),
            payload.public_updates_channel_id?.asSnowflake(),
            false,
            createSnowflake(""),
            client
        ).also { g ->
            client.userImpl.guilds[g.id] = g
            payload.channels.associateByTo(
                client.userImpl.channels,
                { it.id.asSnowflake() },
                { createGuildChannel(it, g) })
        }
    }

    fun createTextChannel(channel : GuildCreateDChannel, guild : GuildImpl) =
        TextChannelImpl(
            channel.id.asSnowflake(),
            client,
            ChannelType.TEXT,
            channel.id.asSnowflake(),
            channel.name,
            channel.position,
            channel.parent_id.asSnowflake(),
            channel.topic
        ).apply { this.guild = guild }

    fun createGuildChannel(channel : GuildCreateDChannel, guild : GuildImpl) : GuildChannel {
        return when (ChannelType.enumById(channel.type)) {

            ChannelType.TEXT -> createTextChannel(channel, guild)


            ChannelType.CATEGORY -> createChannelCategory(channel, guild)


            else -> createTextChannel(channel, guild)

        }
    }

    fun createChannelCategory(channel : GuildCreateDChannel, guild : GuildImpl) : GuildChannel =
        ChannelCategoryImpl(
            client,
            channel.id.asSnowflake(),
            channel.name,
            ChannelType.CATEGORY,
            channel.position
        ).apply { this.guild = guild }

    fun fetchChannel(channelId : Snowflake) : BaseChannel? {
        return client.user.channels[channelId]
    }

    fun createMessage(message : SerialMessage) : Validated<String, Message> {

        var err = ""

        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

        val editedTimestamp = if (!message.edited_timestamp.isNullOrBlank())
            dateFormat.parse(message.edited_timestamp)
        else null

        val mentions = message.mentions.associateBy({ it.id }, { createUser(it) }) as HashMap

        val attachments = message.attachments.associateBy({ it.id }, { createAttachment(it) }) as HashMap

        val channel : TextBasedChannel =
            client.user.textChannels[message.channel_id.asSnowflake()] ?: run {
                err += "Unable to resolve channel."
                return Validated.Invalid(err)
            }

        val guild : Guild? =
            if (message.guild_id == null) null else
                client.user.guilds[message.guild_id.asSnowflake()] ?: run {
                    err += "Unable to resolve guild."
                    return Validated.Invalid(err)
                }

        return Validated.Valid(
            MessageImpl(
                client,
                message.id.asSnowflake(),
                channel,
                guild,
                createUser(message.author),
                message.content,
                dateFormat.parse(message.timestamp),
                editedTimestamp,
                message.tts ?: false,
                message.mention_everyone,
                mentions,
                attachments,
                message.pinned,
                message.id,
                MessageType.enumById(message.type)
            )
        )
    }

    fun createAttachment(attachment : SerialAttachment) : MessageAttachment {
        return MessageAttachment(
            attachment.content_type,
            attachment.filename,
            attachment.height,
            attachment.id,
            attachment.proxy_url,
            attachment.size,
            attachment.url,
            attachment.width
        )
    }

    fun resolveRelationshipType(type : Int) = RelationshipType.values().elementAtOrNull(type) ?: RelationshipType.NONE
    fun createUser(user : SerialUser, relationshipType : Int = 0) : User =
        UserImpl(
            user.username,
            user.discriminator,
            user.avatar,
            user.id.asSnowflake(),
            resolveRelationshipType(relationshipType),
            user.bot,
            client
        )

    fun createCustomStatus(cs : ReadyDCustomStatus) : CustomStatus =
        CustomStatus(
            cs.emoji_id,
            cs.emoji_name,
            cs.expires_at,
            cs.text
        )

    private fun createUserSettings(se : ReadyDUserSettings, client : ClientImpl) : ClientUserSettings =
        ClientUserSettings(
            se.afk_timeout,
            se.allow_accessibility_detection,
            se.animate_emoji,
            se.animate_stickers,
            se.contact_sync_enabled,
            se.convert_emoticons,
            createCustomStatus(se.custom_status),
            se.default_guilds_restricted,
            se.detect_platform_accounts,
            se.developer_mode,
            se.disable_games_tab,
            se.enable_tts_command,
            se.explicit_content_filter,
            se.friend_discovery_flags,
            se.gif_auto_play,
            se.inline_attachment_media,
            se.inline_embed_media,
            se.locale,
            se.message_display_compact,
            se.native_phone_integration_enabled,
            se.passwordless,
            se.render_embeds,
            se.render_reactions,
            // d.user_settings.restricted_guilds,
            listOf(),
            se.show_current_game,
            client.utils.getStatusType(se.status),
            se.stream_notifications_enabled,
            client.utils.getThemeType(se.theme)
        )

    /*
        Super Properties Stuff
     */

    var superProperties = SuperProperties()
    var superPropertiesStr = ""
    var superPropertiesB64 = ""

    fun createSuperProperties() {
        superProperties = SuperProperties(
            "Windows",
            "Chrome",
            "",
            "en-US",
            userAgent,
            clientVersion,
            "10",
            "",
            "",
            "",
            "",
            "stable",
            clientBuildNumber
        )
        superPropertiesStr = json.encodeToString(superProperties)
        superPropertiesB64 = Base64.getEncoder().encodeToString(superPropertiesStr.toByteArray())
    }

    fun createDMChannel(channel : ReadyDPrivateChannel) : DMChannelImpl =
        DMChannelImpl(
            channel.id.asSnowflake(),
            client,
            ChannelType.enumById(channel.type),
            createSnowflake(channel.last_message_id),
            channel.name.ifBlank { channel.recipients.first().username },
            channel.recipients.associateBy(
                { r -> r.id },
                { r -> client.utils.createUser(r) }
            )
        )

    suspend fun fetchUser(id : String) : User {
        val response = client.httpClient.get("$BASE_URL/users/$id").await()
        return createUser(json.decodeFromString(response))
    }

    suspend fun fetchMessageById(id : String, channel : TextBasedChannel) : Message? {
        val response = client.httpClient.get("$BASE_URL/channels/${channel.id.asString()}/messages/$id").await()
        return when (val result = client.utils.createMessage(json.decodeFromString(response))) {
            is Invalid -> {
                log("Error in messageCreate: ${result.left()}", level = LogLevel(LoggerLevel.LOW, client))
                null
            }

            is Valid -> {
                result.value
            }
        }
    }

    suspend fun fetchLastMessageInChannel(
        channel : TextBasedChannel,
        user : User,
        filters : MessageSearchFilters,
    ) : Message? {
        var parameters = ""
        if (filters.before_id.asString().isNotBlank()) parameters += "max_id=${filters.before_id}&"
        if (filters.after_id.asString().isNotBlank()) parameters += "after=${filters.after_id}&"
        if (filters.mentioning_user_id.asString().isNotBlank()) parameters += "mentions=${filters.mentioning_user_id}&"
        try {
            val lastMessageResponse =
                client.httpClient.get("$BASE_URL/channels/${channel.id.asString()}/messages/search?author_id=${user.id.asString()}&$parameters")
                    .await()
            json.parseToJsonElement(lastMessageResponse).jsonObject["messages"]?.jsonArray?.forEach { it ->
                val messages = json.decodeFromJsonElement<List<SerialMessage>>(it)
                if (messages.isEmpty()) {
                    return null
                }
                if (messages.none { it.author.id.asSnowflake() == user.id && it.type == 0 || it.type == 19 }) {
                    filters.before_id = messages.last().id.asSnowflake()
                    delay(500)
                    fetchLastMessageInChannel(channel, user, filters)
                }
                val message = messages.first { it.author.id.asSnowflake() == user.id && it.type == 0 || it.type == 19 }
                return when (val result = createMessage(message)) {
                    is Invalid -> {
                        log("Error in messageCreate: ${result.left()}", level = LogLevel(LoggerLevel.LOW, client))
                        null
                    }

                    is Valid -> {
                        result.value
                    }
                }
            }
        } catch (e : Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun createClientUserImpl(d : ReadyD) =
        ClientUserImpl(
            d.user.verified,
            d.user.username,
            d.user.discriminator,
            d.user.id.asSnowflake(),
            d.user.email,
            d.user.bio,
            createUserSettings(d.user_settings, client),
            d.user.avatar,
            d.user.premium,
            client.configuration.token,
            RelationshipType.NONE,
            d.user.bot,
            client,
            client
        )
}
