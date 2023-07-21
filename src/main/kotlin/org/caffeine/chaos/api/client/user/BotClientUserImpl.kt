package org.caffeine.chaos.api.client.user

import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientImpl
import org.caffeine.chaos.api.client.connection.payloads.gateway.SerialMessage
import org.caffeine.chaos.api.entities.Snowflake
import org.caffeine.chaos.api.entities.channels.DMChannel
import org.caffeine.chaos.api.entities.channels.GuildChannel
import org.caffeine.chaos.api.entities.channels.TextBasedChannel
import org.caffeine.chaos.api.entities.message.Message
import org.caffeine.chaos.api.typedefs.RelationshipType
import org.caffeine.chaos.api.utils.BASE_URL
import org.caffeine.chaos.api.utils.MessageSendData
import org.caffeine.chaos.api.utils.json

data class BotClientUserImpl(
    override val bio : String?,
    override val token : String,
    override val client : Client,
    override val username : String,
    override val id : Snowflake,
    override val avatar : String?,
    override val bot : Boolean,
    override val relation : RelationshipType = RelationshipType.NONE,
) : BotClientUser, BaseClientUserImpl(bio, token, client, username, id, avatar, bot) {

    override var asMention : String = "<@${id}>"

    suspend fun sendMessageWithAttachments(
        channel : TextBasedChannel,
        message : MessageSendData,
    ) : CompletableDeferred<Message> {
        client as ClientImpl
        var files = -1
        val data = json.encodeToString(message)
        val body = formData {

            append("payload_json", data, Headers.build {
                append(HttpHeaders.ContentType, ContentType.Application.Json)
                append(HttpHeaders.ContentDisposition, "form-data; name=\"payload_json\"")
            })

            message.byteAttachments.map {
                files++
                val type = ContentType.fromFileExtension(it.key.replaceBefore(".", "")).toString()
                append("files[$files]", it.value, Headers.build {
                    append(HttpHeaders.ContentType, type)
                    append(HttpHeaders.ContentDisposition, "form-data; name=\"files[$files]\"; filename=\"${it.key}\"")
                })
            }
        }
        val response =
            client.httpClient.client.submitFormWithBinaryData(url = "$BASE_URL/channels/${channel.id}/messages", body)
                .bodyAsText()
        val serial = json.decodeFromString<SerialMessage>(response)
        return CompletableDeferred(client.utils.createMessage(serial))
    }

    override suspend fun sendMessage(
        channel : TextBasedChannel,
        message : MessageSendData,
    ) : CompletableDeferred<Message> {
        if (message.attachments.isNotEmpty()) return sendMessageWithAttachments(channel, message)
        client as ClientImpl
        val data = json.encodeToString(message)
        val response = client.httpClient.post("$BASE_URL/channels/${channel.id}/messages", data).await()
        val serial = json.decodeFromString<SerialMessage>(response)
        return CompletableDeferred(client.utils.createMessage(serial))
    }

    override val dmChannels : Map<Snowflake, DMChannel>
        get() = channels.values.filterIsInstance<DMChannel>().associateBy { it.id }

    override val textChannels : Map<Snowflake, TextBasedChannel>
        get() = channels.values.filterIsInstance<TextBasedChannel>().associateBy { it.id }

    override val guildChannels : Map<Snowflake, GuildChannel>
        get() = channels.values.filterIsInstance<GuildChannel>().associateBy { it.id }

}