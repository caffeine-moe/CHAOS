package org.caffeine.chaos.api.utils

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
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
import org.caffeine.chaos.api.models.users.User
import org.caffeine.chaos.api.payloads.gateway.data.SerialAttachment
import org.caffeine.chaos.api.payloads.gateway.data.SerialMessage
import org.caffeine.chaos.api.payloads.gateway.data.SerialUser
import org.caffeine.chaos.api.payloads.gateway.data.guild.create.GuildCreateD
import org.caffeine.chaos.api.payloads.gateway.data.guild.create.GuildCreateDChannel
import org.caffeine.chaos.api.typedefs.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue
import kotlin.system.exitProcess

open class DiscordUtils {

    lateinit var token : String
    lateinit var client : ClientImpl

    @Serializable
    data class SuperProperties(
        var os : String = "",
        var browser : String = "",
        var device : String = "",
        var browser_user_agent : String = "",
        var browser_version : String = "",
        var os_version : String = "",
        var referrer : String = "",
        var referring_domain : String = "",
        var referrer_current : String = "",
        var referring_domain_current : String = "",
        var release_channel : String = "",
        var system_locale : String = "",
        var client_build_number : Int = 0,
        var client_event_source : JsonObject = json.decodeFromString("{}"),
    )

    var gatewaySequence = 0

    var sessionId = ""

    /*
        Http Client
     */

    val discordHTTPClient : HttpClient = HttpClient(CIO) {
        install(WebSockets)
        install(HttpCookies)
        install(HttpCache)
        install(ContentNegotiation)
        install(DefaultRequest)
        install(HttpRequestRetry) {
            maxRetries = 5
            retryIf { _, response ->
                response.status.value == 429
            }
            delayMillis(respectRetryAfterHeader = true) { delay ->
                delay * 1000L
            }
        }

        defaultRequest {
            port = 443
            headers {
                append("Accept-Language", "en-US")
                append("Authorization", token)
                append("Cache-Control", "no-cache")
                append("Connection", "keep-alive")
                append("Origin", "https://discord.com")
                append("Pragma", "no-cache")
                append("Referer", "https://discord.com/channels/@me")
                append("Sec-CH-UA", "\"(Not(A:Brand\";v=\"8\", \"Chromium\";v=\"$clientVersion\"")
                append("Sec-CH-UA-Mobile", "?0")
                append("Sec-CH-UA-Platform", "Windows")
                append("Sec-Fetch-Dest", "empty")
                append("Sec-Fetch-Mode", "cors")
                append("Sec-Fetch-Site", "same-origin")
                append("User-Agent", userAgent)
                append("X-Discord-Locale", "en-US")
                append("X-Debug-Options", "bugReporterEnabled")
                append("X-Super-Properties", superPropertiesB64)
            }
        }
        engine {
            pipelining = true
        }
        expectSuccess = true

        HttpResponseValidator {
            handleResponseExceptionWithRequest { cause, request ->
                if (cause.localizedMessage.contains("401 Unauthorized.")) {
                    log("Invalid token, please update your config with a valid token.", "API:")
                    exitProcess(69)
                }
                log("Error: ${cause.message} Request: ${request.content}", "API:")
                return@handleResponseExceptionWithRequest
            }
        }
    }

    fun calcNonce(id : Long = 0) : String {
        val unixTs = if (id == 0L) System.currentTimeMillis() else id
        return ((unixTs - 1420070400000) * 4194304).absoluteValue.toString()
    }

    suspend fun tokenValidator(token : String) {
        discordHTTPClient.get("$BASE_URL/users/@me") {
            headers {
                append(HttpHeaders.Authorization, token)
            }
        }
    }

    fun getStatusType(type : String) : StatusType {
        StatusType.values().forEach {
            if (it.value == type.lowercase()) {
                return it
            }
        }
        return StatusType.UNKNOWN
    }

    fun getChannelType(type : Number) : ChannelType {
        ChannelType.values().forEach {
            if (it.ordinal == type) {
                return it
            }
        }
        return ChannelType.UNKNOWN
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

    fun getMessageType(type : Number) : MessageType {
        MessageType.values().forEach {
            if (it.ordinal == type) {
                return it
            }
        }
        return MessageType.UNKNOWN
    }

    private fun String.isValidSnowflake() : Boolean {
        val unix = client.user.convertIdToUnix(this)
        return unix <= System.currentTimeMillis() && unix > 1420070400000
    }

    suspend fun fetchMessages(channel : TextBasedChannel, filters : MessageFilters) : List<Message> {
        val collection : MutableList<Message> = arrayListOf()
        val messagesPerRequest = 100

        try {

            if (filters.author_id == client.user.id && filters.before_id.isBlank()) {
                val lastMessageResponse =
                    discordHTTPClient.get("$BASE_URL/channels/${channel.id}/messages/search?author_id=${client.user.id}&limit=1") {}
                json.parseToJsonElement(lastMessageResponse.bodyAsText()).jsonObject["messages"]?.jsonArray?.forEach {
                    filters.before_id = json.decodeFromJsonElement<List<SerialMessage>>(it).first().id
                }
            }

            while (true) {
                var parameters = ""
                parameters += if (filters.limit > 0) "limit=${messagesPerRequest.coerceAtMost(filters.limit - collection.size)}&"
                else "limit=${messagesPerRequest}&"
                if (filters.before_id.isNotBlank()) parameters += "before=${filters.before_id}&"
                if (filters.after_id.isNotBlank()) parameters += "after=${filters.after_id}&"
                if (filters.author_id.isNotBlank()) parameters += "author_id=${filters.author_id}&"
                if (filters.mentioning_user_id.isNotBlank()) parameters += "mentions=${filters.mentioning_user_id}&"
                val response = discordHTTPClient.request("$BASE_URL/channels/${channel.id}/messages?${parameters}") {
                    method = HttpMethod.Get
                    headers {
                        append(HttpHeaders.Authorization, token)
                        append(HttpHeaders.ContentType, "application/json")
                    }
                }
                val newMessages = json.decodeFromString<MutableList<SerialMessage>>(response.bodyAsText())
                newMessages.removeIf { filters.author_id.isNotBlank() && it.author.id != filters.author_id }
                newMessages.forEach { collection += createMessage(it) }
                filters.before_id = collection.last().id

                if (filters.needed != 0 && collection.size >= filters.needed)
                    break

                if (newMessages.size < messagesPerRequest)
                    break

                println(collection.size)

                delay(200)
            }
        } catch (e : Exception) {
            log("Error: ${e.message}", "API:")
            e.printStackTrace()
        }
        return collection
    }

    suspend fun fetchPrivateChannel(id : String) : DMChannel? {
        return client.user.dmChannels[id]
    }

    suspend fun fetchGuild(id : String) : Guild? {
        var guild : Guild? = null

        if (id.isValidSnowflake() && client.user.guilds.containsKey(id)) {
            guild = client.user.guilds[id]
        } else if (id.isValidSnowflake()) {
            val response = discordHTTPClient.get("$BASE_URL/guilds/$id") {
                headers {
                    append(HttpHeaders.Authorization, token)
                }
            }
            guild = createGuild(json.decodeFromString(response.bodyAsText()))
        }
        return guild
    }

    fun createGuild(payload : GuildCreateD) : Guild? {
        var guild : Guild?
        payload.channels.forEach {
            val channel = createGuildChannel(it)
            client.userImpl._channels.put(
                it.id,
                channel
            )
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
                client.client,
            )
        } catch (e : Exception) {
            log("Error creating guild: ${e.message}", "API:")
            guild = null
        }
        return guild
    }

    fun createGuildChannel(channel : GuildCreateDChannel) : GuildChannel {
        when (channel.type) {
            ChannelType.TEXT.ordinal -> {
                return TextChannel(
                    channel.id,
                    client.client,
                    ChannelType.TEXT,
                    channel.id,
                    Date(),
                    channel.name
                )
            }
        }
        return TextChannel()
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
            //TextChannel(d.channel_id, client.client),
            (client.utils.fetchChannel(message.channel_id)
            //shouldn't happen ever but just in case
                ?: TextChannel(message.channel_id)) as TextBasedChannel,
            client.utils.fetchGuild(message.guild_id ?: ""),
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
            getMessageType(message.type),
        )
    }

    fun createChannel() {

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
            attachment.width,
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
            userAgent,
            clientVersion,
            "10",
            "",
            "",
            "",
            "",
            "stable",
            "en-US",
            clientBuildNumber
        )
        superPropertiesStr = json.encodeToString(superProperties)
        superPropertiesB64 = Base64.getEncoder().encodeToString(superPropertiesStr.toByteArray())
    }

    suspend fun fetchUser(id : String) : DiscordUser {
        val response = client.utils.discordHTTPClient.get("$BASE_URL/users/$id").bodyAsText()
        return createUser(json.decodeFromString(response))
    }

    fun fetchTextChannel(channelId : String) : TextBasedChannel? {
        return client.user.channels.filter { it.value.type == ChannelType.TEXT }[channelId] as TextBasedChannel?
    }
}

class MessageBuilder : DiscordUtils() {
    private var sb = StringBuilder()
    private var tts = false

    //private var attachments = mutableListOf<MessageAttachment>()
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

