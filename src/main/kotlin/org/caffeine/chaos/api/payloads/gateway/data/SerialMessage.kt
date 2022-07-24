package org.caffeine.chaos.api.payloads.gateway.data

import kotlinx.serialization.Serializable

@Serializable
data class SerialMessage(
    val attachments : List<SerialAttachment> = mutableListOf(),
    val author : SerialUser,
    val channel_id : String,
    val content : String,
    val edited_timestamp : String?,
    val flags : Int?,
    val id : String,
    val mention_everyone : Boolean = false,
    val mention_roles : List<String> = mutableListOf(),
    val mentions : List<SerialUser> = mutableListOf(),
    val pinned : Boolean,
    val referenced_message : SerialMessage? = null,
    val timestamp : String?,
    val tts : Boolean?,
    val type : Int,
    val guild_id : String? = null,
)