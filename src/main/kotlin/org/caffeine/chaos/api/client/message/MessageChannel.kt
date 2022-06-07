package org.caffeine.chaos.api.client.message

import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.caffeine.chaos.api.*
import org.caffeine.chaos.api.client.ClientGuild
import org.caffeine.chaos.api.client.DiscordChannelType
import org.caffeine.chaos.api.client.slashcommands.AppCommand
import org.caffeine.chaos.api.client.slashcommands.Data
import org.caffeine.chaos.api.client.slashcommands.SendAppCommand
import org.caffeine.chaos.api.client.utils.calcNonce
import org.caffeine.chaos.api.client.utils.webkitBoundary
import java.util.concurrent.CompletableFuture


class MessageChannel(
    var id : String,
) {

    @Serializable
    private data class GetGuildResponse(
        @SerialName("flags")
        val flags : Int = 0,
        @SerialName("guild_id")
        val guildId : String = "",
        @SerialName("id")
        val id : String = "",
        @SerialName("last_message_id")
        val lastMessageId : String = "",
        @SerialName("name")
        val name : String = "",
        @SerialName("nsfw")
        val nsfw : Boolean = false,
        @SerialName("parent_id")
        val parentId : String = "",
        @SerialName("permission_overwrites")
        val permissionOverwrites : List<PermissionOverwrite> = listOf(),
        @SerialName("position")
        val position : Int = 0,
        @SerialName("rate_limit_per_user")
        val rateLimitPerUser : Int = 0,
        @SerialName("topic")
        val topic : String? = null,
        @SerialName("type")
        val type : Int = 0,
    )

    @Serializable
    private data class PermissionOverwrite(
        @SerialName("allow")
        val allow : String = "",
        @SerialName("deny")
        val deny : String = "",
        @SerialName("id")
        val id : String = "",
        @SerialName("type")
        val type : Int = 0,
    )

    suspend fun getGuild() : ClientGuild? {
        val re = discordHTTPClient.get(
            "$BASE_URL/channels/${id}"
        ) {
            headers {
                append(HttpHeaders.Authorization, token)
            }
        }
        var thing : ClientGuild?
        val guildId = json.decodeFromString<GetGuildResponse>(re.bodyAsText()).guildId
        thing = ClientGuild("", guildId)
        if (guildId.isBlank()) thing = null
        return thing
    }

    @Serializable
    data class TypeResponse(
        val type : Int,
    )

    @Serializable
    data class CommandSearchResponse(
        val application_commands : List<AppCommand>,
    )

    private suspend fun getCommand(name : String) : AppCommand {
        val re = discordHTTPClient.get(
            "$BASE_URL/channels/${id}/application-commands/search?type=1&query=$name&limit=7&include_applications=false"
        ) {
            headers {
                append(HttpHeaders.Authorization, token)
            }
        }
        return json.decodeFromString<CommandSearchResponse>(re.bodyAsText()).application_commands.first()
    }

    suspend fun sendInteraction(name : String, guildId : String) : CompletableFuture<String> {
        val command = getCommand(name)
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
            SendAppCommand(2, command.applicationId, guildId, id, sid, data, calcNonce())
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