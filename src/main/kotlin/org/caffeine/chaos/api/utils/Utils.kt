package org.caffeine.chaos.api.utils

import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

internal const val GATEWAY = "gateway.discord.gg"
internal const val BASE_URL = "https://discord.com/api/v9"

private val messageModule = SerializersModule {
    polymorphic(MessageData::class) {
        subclass(MessageBuilder::class)
    }
}

internal val json = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
    coerceInputValues = true
    prettyPrint = true
    serializersModule = messageModule
}

internal inline fun <reified T> tryDecodeFromString(payload : String) : T? =
    try {
        json.decodeFromString<T>(payload)
    } catch (e : SerializationException) {
        null
    }