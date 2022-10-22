package org.caffeine.chaos.api.utils

import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import org.caffeine.chaos.api.BASE_URL
import org.caffeine.chaos.api.client.ClientImpl
import org.caffeine.chaos.api.client.connection.payloads.gateway.SerialAttachment
import org.caffeine.chaos.api.client.connection.payloads.gateway.SerialMessage
import org.caffeine.chaos.api.client.connection.payloads.gateway.SerialUser
import org.caffeine.chaos.api.client.connection.payloads.gateway.guild.create.GuildCreateD
import org.caffeine.chaos.api.client.connection.payloads.gateway.guild.create.GuildCreateDChannel
import org.caffeine.chaos.api.json
import org.caffeine.chaos.api.models.channels.DMChannel
import org.caffeine.chaos.api.models.channels.TextChannel
import org.caffeine.chaos.api.models.guild.Guild
import org.caffeine.chaos.api.models.interfaces.BaseChannel
import org.caffeine.chaos.api.models.interfaces.DiscordUser
import org.caffeine.chaos.api.models.interfaces.GuildChannel
import org.caffeine.chaos.api.models.interfaces.TextBasedChannel
import org.caffeine.chaos.api.models.message.Message
import org.caffeine.chaos.api.models.message.MessageAttachment
import org.caffeine.chaos.api.models.message.MessageFilters
import org.caffeine.chaos.api.models.message.MessageSearchFilters
import org.caffeine.chaos.api.models.users.User
import org.caffeine.chaos.api.typedefs.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue

open class DiscordUtils {

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



    fun calcNonce(id : Long = 0) : String {
        val unixTs = if (id == 0L) System.currentTimeMillis() else id
        return ((unixTs - 1420070400000) * 4194304).absoluteValue.toString()
    }

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

    private fun String.isValidSnowflake() : Boolean {
        val unix = client.user.convertIdToUnix(this)
        return unix <= System.currentTimeMillis() && unix > 1420070400000
    }

    suspend fun fetchMessages(channel : TextBasedChannel, filters : MessageFilters) : List<Message> {
        val collection : MutableList<Message> = arrayListOf()
        try {
            if (filters.author_id == client.user.id && filters.before_id.isBlank()) {
                fetchLastMessageInChannel(channel, client.user, MessageSearchFilters())?.let {
                    collection.add(it)
                    filters.before_id = it.id
                }
            }

            while (true) {
                var parameters = ""
                if (filters.before_id.isNotBlank()) parameters += "before=${filters.before_id}&"
                if (filters.after_id.isNotBlank()) parameters += "after=${filters.after_id}&"
                if (filters.author_id.isNotBlank()) parameters += "author_id=${filters.author_id}&"
                if (filters.mentioning_user_id.isNotBlank()) parameters += "mentions=${filters.mentioning_user_id}&"
                val response = client.httpClient.get("$BASE_URL/channels/${channel.id}/messages?$parameters").await()
                val newMessages = json.decodeFromString<MutableList<SerialMessage>>(response)
                newMessages.removeIf { filters.author_id.isNotBlank() && it.author.id != filters.author_id }
                if (newMessages.size == 0) {
                    val lastmessage = fetchLastMessageInChannel(
                        channel,
                        client.user,
                        MessageSearchFilters(before_id = filters.before_id)
                    )
                        ?: return collection
                    collection += lastmessage
                } else {
                    newMessages.forEach { collection += createMessage(it) }
                }

                filters.before_id = collection.last().id

                if (filters.needed != 0 && collection.size >= filters.needed) {
                    break
                }

                delay(500)
            }
        } catch (e : Exception) {
            log("Error: ${e.message}", "API:")
            e.printStackTrace()
        }
        return if (collection.size <= filters.needed) {
            collection
        } else {
            collection.take(filters.needed)
        }
    }

    suspend fun fetchPrivateChannel(id : String) : DMChannel? {
        return client.user.dmChannels[id]
    }

    suspend fun fetchGuild(id : String) : Guild? {
        var guild : Guild? = null

        if (id.isValidSnowflake() && client.user.guilds.containsKey(id)) {
            guild = client.user.guilds[id]
        } else if (id.isValidSnowflake()) {
            val response = client.httpClient.get("$BASE_URL/guilds/$id").await()
            guild = createGuild(json.decodeFromString(response))
        }
        return guild
    }

    fun createGuild(payload : GuildCreateD) : Guild? {
        var guild : Guild?
        payload.channels.forEach {
            val channel = createGuildChannel(it)
            client.userImpl.channels[it.id] = channel
        }
        try {
            guild = Guild(
                payload.id,
                payload.name,
                payload.icon,
                payload.description,
                payload.splash,
                payload.discovery_splash,
                payload.banner,
                payload.owner_id,
                payload.application_id,
                payload.region,
                payload.afk_channel_id,
                payload.afk_timeout,
                payload.system_channel_id,
                payload.widget_enabled,
                "",
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
                payload.rules_channel_id,
                payload.public_updates_channel_id,
                false,
                "",
                client.client
            )
        } catch (e : Exception) {
            log("Error creating guild: ${e.message}", "API:")
            guild = null
        }
        return guild
    }

    fun createGuildChannel(channel : GuildCreateDChannel) : GuildChannel {
        when (ChannelType.enumById(channel.type)) {
            ChannelType.TEXT -> {
                return TextChannel(
                    channel.id,
                    client.client,
                    ChannelType.TEXT,
                    channel.id,
                    Date(),
                    channel.name,
                    channel.position,
                    channel.parent_id,
                    channel.topic
                )
            } else -> {
                return TextChannel(client = client.client)
            }
        }
    }

    fun fetchChannel(channelId : String) : BaseChannel? {
        return client.user.channels[channelId]
    }

    suspend fun createMessage(message : SerialMessage) : Message {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

        val editedTimestamp = if (!message.edited_timestamp.isNullOrBlank()) {
            dateFormat.parse(message.edited_timestamp)
        } else {
            null
        }

        val mentions = hashMapOf<String, User>()

        val attachmeents = hashMapOf<String, MessageAttachment>()

        for (mention in message.mentions) {
            mentions[mention.id] = createUser(mention)
        }

        for (attachment in message.attachments) {
            attachmeents[attachment.id] = createAttachment(attachment)
        }

        return Message(
            client.client,
            message.id,
            (
                    fetchChannel(message.channel_id)
                    // shouldn't happen ever but just in case
                        ?: TextChannel(message.channel_id, client = client.client)
                    ) as TextBasedChannel,
            fetchGuild(message.guild_id ?: ""),
            User(
                message.author.username,
                message.author.discriminator,
                message.author.avatar,
                message.author.id,
                message.author.bot,
                client.client
            ),
            message.content,
            dateFormat.parse(message.timestamp),
            editedTimestamp,
            message.tts ?: false,
            message.mention_everyone,
            mentions,
            attachmeents,
            message.pinned,
            MessageType.enumById(message.type)
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

    fun createUser(user : SerialUser) : User {
        return User(
            user.username,
            user.discriminator,
            user.avatar,
            user.id,
            user.bot,
            client.client
        )
    }

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

    suspend fun fetchUser(id : String) : DiscordUser {
        val response = client.httpClient.get("$BASE_URL/users/$id").await()
        return createUser(json.decodeFromString(response))
    }

    fun fetchTextChannel(channelId : String) : TextBasedChannel? {
        return client.user.channels.filter { it.value.type == ChannelType.TEXT }[channelId] as TextBasedChannel?
    }

    suspend fun fetchMessageById(id : String, channel : TextBasedChannel) : Message? {
        if (id.isValidSnowflake()) {
            val response = client.httpClient.get("$BASE_URL/channels/${channel.id}/messages/$id").await()
            return createMessage(json.decodeFromString(response))
        }
        return null
    }

    suspend fun fetchLastMessageInChannel(
        channel : TextBasedChannel,
        user : DiscordUser,
        filters : MessageSearchFilters,
    ) : Message? {
        var parameters = ""
        if (filters.before_id.isNotBlank()) parameters += "max_id=${filters.before_id}&"
        if (filters.after_id.isNotBlank()) parameters += "after=${filters.after_id}&"
        if (filters.mentioning_user_id.isNotBlank()) parameters += "mentions=${filters.mentioning_user_id}&"
        try {
            val lastMessageResponse =
                client.httpClient.get("$BASE_URL/channels/${channel.id}/messages/search?author_id=${user.id}&$parameters")
                    .await()
            json.parseToJsonElement(lastMessageResponse).jsonObject["messages"]?.jsonArray?.forEach { it ->
                val messages = json.decodeFromJsonElement<List<SerialMessage>>(it)
                if (messages.isEmpty()) {
                    return null
                }
                if (messages.none { it.author.id == user.id && it.type == 0 || it.type == 19 }) {
                    filters.before_id = messages.last().id
                    delay(500)
                    fetchLastMessageInChannel(channel, user, filters)
                }
                val message = messages.first { it.author.id == user.id && it.type == 0 || it.type == 19 }
                return createMessage(message)
            }
        } catch (e : Exception) {
            e.printStackTrace()
        }
        return null
    }
}

class MessageBuilder : DiscordUtils() {
    private var sb = StringBuilder()
    private var tts = false

    // private var attachments = mutableListOf<MessageAttachment>()
    fun build() : MessageOptions {
        return MessageOptions(sb.toString(), tts, calcNonce())
    }

    fun append(text : String) : MessageBuilder {
        sb.append(text)
        return this
    }

    fun appendLine(text : String) : MessageBuilder {
        sb.appendLine(text)
        return this
    }

    /*        fun addAttachment(attachment : MessageAttachment) : MessageBuilder {
                attachments.add(attachment)
                return this
            }*/
}
