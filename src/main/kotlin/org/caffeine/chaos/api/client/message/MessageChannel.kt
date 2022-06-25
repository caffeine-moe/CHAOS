package org.caffeine.chaos.api.client.message

import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.caffeine.chaos.api.BASE_URL
import org.caffeine.chaos.api.client.DiscordChannelType
import org.caffeine.chaos.api.client.slashcommands.AppCommand
import org.caffeine.chaos.api.client.slashcommands.Data
import org.caffeine.chaos.api.client.slashcommands.SendAppCommand
import org.caffeine.chaos.api.utils.discordHTTPClient
import org.caffeine.chaos.api.json
import org.caffeine.chaos.api.token
import org.caffeine.chaos.api.utils.calcNonce
import org.caffeine.chaos.api.utils.sessionId
import org.caffeine.chaos.api.utils.webkitBoundary
import java.util.concurrent.CompletableFuture

@Serializable
open class MessageChannel(
    @Transient open var id : String = "",
) {

    @Serializable
    data class TypeResponse(
        val type : Int,
    )

    @Serializable
    data class CommandSearchResponse(
        val application_commands : List<AppCommand>,
    )

    suspend fun getAppCommand(name : String) : List<AppCommand> {
        val re = discordHTTPClient.get(
            "$BASE_URL/channels/${id}/application-commands/search?type=1&query=$name&limit=7&include_applications=false"
        ) {
            headers {
                append(HttpHeaders.Authorization, token)
            }
        }
        return json.decodeFromString<CommandSearchResponse>(re.bodyAsText()).application_commands
    }

    suspend fun sendInteraction(command : AppCommand, guildId : String) : CompletableFuture<String> {
        val data = Data(
            command.version,
            command.id,
            command.name,
            command.type,
            emptyArray(),
            command,
            emptyArray()
        )
        val toSend = json.encodeToString(
            SendAppCommand(2, command.applicationId, guildId, id, sessionId, data, calcNonce())
        )
        discordHTTPClient.post("$BASE_URL/interactions") {
            setBody(MultiPartFormDataContent(
                formData {
                    append("form-data", toSend, Headers.build {
                        append(HttpHeaders.ContentType, "application/json")
                        append(HttpHeaders.ContentDisposition, "name=\"payload_json\"")
                    })

                },
                boundary = webkitBoundary()
            )
            )
            headers {
                append(HttpHeaders.Authorization, token)
            }
        }
        return CompletableFuture.completedFuture("done")
    }

    suspend fun messagesAsCollection(filters : MessageFilters) : Collection<Message> {
        val collection : MutableList<Message> = mutableListOf()
        val messagesPerRequest = 100
        while (true) {
            var parameters = ""
            parameters += if (filters.limit > 0) "limit=${messagesPerRequest.coerceAtMost(filters.limit - collection.size)}&"
            else "limit=${messagesPerRequest}&"
            if (filters.before_id.isNotBlank()) parameters += "before=${filters.before_id}&"
            if (filters.after_id.isNotBlank()) parameters += "after=${filters.after_id}&"
            if (filters.author_id.isNotBlank()) parameters += "author_id=${filters.author_id}&"
            if (filters.mentioning_user_id.isNotBlank()) parameters += "mentions=${filters.mentioning_user_id}&"
            val response = discordHTTPClient.request("$BASE_URL/channels/${this.id}/messages?${parameters}") {
                method = HttpMethod.Get
                headers {
                    append(HttpHeaders.Authorization, token)
                    append(HttpHeaders.ContentType, "application/json")
                }
            }
            val newMessages = json.decodeFromString<List<Message>>(response.bodyAsText())
            collection.addAll(newMessages)
            filters.before_id = collection.last().id
            collection.removeIf { filters.author_id.isNotBlank(); it.author.id != filters.author_id }

            if (filters.needed != 0 && collection.size >= filters.needed)
                break

            if (newMessages.size < messagesPerRequest)
                break

            withContext(Dispatchers.IO) {
                Thread.sleep(200)
            }
        }
        return collection
    }

    suspend fun sendMessage(message : Message) : CompletableFuture<Message> {
        return sendMessage(this, message)
    }

    suspend fun type() : DiscordChannelType {
        val response = discordHTTPClient.request("$BASE_URL/channels/${this.id}") {
            method = HttpMethod.Get
            headers {
                append(HttpHeaders.Authorization, token)
            }
        }
        val type = when (json.decodeFromString<TypeResponse>(response.bodyAsText()).type) {
            1 -> return DiscordChannelType.DM
            3 -> return DiscordChannelType.GROUP
            else -> {
                DiscordChannelType.GUILD
            }
        }
        return type
    }

    suspend fun delete() {
        discordHTTPClient.request("$BASE_URL/channels/${this.id}") {
            method = HttpMethod.Delete
            headers {
                append(HttpHeaders.Authorization, token)
            }
        }
    }
}