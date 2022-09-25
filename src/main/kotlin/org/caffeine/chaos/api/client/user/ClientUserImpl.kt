package org.caffeine.chaos.api.client.user

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import org.caffeine.chaos.api.BASE_URL
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientImpl
import org.caffeine.chaos.api.json
import org.caffeine.chaos.api.models.guild.Guild
import org.caffeine.chaos.api.models.interfaces.BaseChannel
import org.caffeine.chaos.api.models.interfaces.TextBasedChannel
import org.caffeine.chaos.api.models.message.Message
import org.caffeine.chaos.api.models.message.MessageFilters
import org.caffeine.chaos.api.payloads.gateway.data.SerialMessage
import org.caffeine.chaos.api.utils.log
import kotlin.math.absoluteValue

data class ClientUserImpl(
    override var verified : Boolean,
    override var username : String,
    override var discriminator : String,
    override var id : String,
    override var email : String?,
    override var bio : String?,
    override var settings : ClientUserSettings,
    override var avatar : String?,
    override var relationships : ClientUserRelationships,
    override var premium : Boolean,
    override var token : String,
    override val bot : Boolean,
    override val client : Client,
    override val clientImpl : ClientImpl,
) : BaseClientUser {

    override val discriminatedName : String
        get() = "$username#$discriminator"

    override fun avatarUrl() : String {
        return if (!avatar.isNullOrBlank()) {
            if (avatar!!.startsWith("a_")) {
                "https://cdn.discordapp.com/avatars/$id/$avatar.gif?size=4096"
            } else {
                "https://cdn.discordapp.com/avatars/$id/$avatar.png?size=4096"
            }
        } else {
            "https://cdn.discordapp.com/embed/avatars/${discriminator.toInt().absoluteValue % 5}.png"
        }
    }

    var _channels = HashMap<String, BaseChannel>()
    override val channels : Map<String, BaseChannel>
        get() = _channels

    var _guilds = HashMap<String, Guild>()
    override val guilds : Map<String, Guild>
        get() = _guilds


    override suspend fun fetchMessagesFromChannel(
        channel : TextBasedChannel,
        filters : MessageFilters,
    ) : List<Message> {
        val collection : MutableList<Message> = arrayListOf()
        val messagesPerRequest = 100

        try {

            if (filters.author_id == client.user.id && filters.before_id.isBlank()) {
                val lastMessageResponse =
                    clientImpl.utils.discordHTTPClient.get("$BASE_URL/channels/${channel.id}/messages/search?author_id=${client.user.id}&limit=1") {}
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
                val response =
                    clientImpl.utils.discordHTTPClient.request("$BASE_URL/channels/${channel.id}/messages?${parameters}") {
                        method = HttpMethod.Get
                        headers {
                            append(HttpHeaders.Authorization, token)
                            append(HttpHeaders.ContentType, "application/json")
                        }
                    }
                val newMessages = json.decodeFromString<MutableList<SerialMessage>>(response.bodyAsText())
                newMessages.removeIf { filters.author_id.isNotBlank() && it.author.id != filters.author_id }
                newMessages.forEach { collection += clientImpl.utils.createMessage(it) }
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

    override suspend fun fetchChannelFromId(id : String) : BaseChannel? {
        return this._channels[id]
    }

}