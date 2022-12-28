package org.caffeine.chaos.api.client.connection.payloads.gateway

import kotlinx.serialization.Serializable

@Serializable
data class SerialMessageUpdate(
    val attachments : List<SerialAttachment> = mutableListOf(),
    val content : String? = null,
    val edited_timestamp : String? = null,
    val id : String,
    val mention_everyone : Boolean = false,
    val mention_roles : List<String> = mutableListOf(),
    val mentions : List<SerialUser> = mutableListOf(),
    val pinned : Boolean? = null,
    val timestamp : String? = null,
    val tts : Boolean? = null,
)