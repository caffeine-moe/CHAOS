package org.caffeine.chaos.api.utils

import kotlinx.coroutines.CompletableDeferred
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

internal const val GATEWAY = "gateway.discord.gg"
internal const val BASE_URL = "https://discord.com/api/v9"
internal const val CDN_URL = "https://cdn.discordapp.com/"

private val messageModule = SerializersModule {
    polymorphic(MessageData::class) {
        subclass(MessageBuilder::class)
    }
    polymorphic(MessageSendData::class) {
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

internal val jsonNoDefaults = Json {
    ignoreUnknownKeys = true
    encodeDefaults = false
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

suspend inline fun <reified T> CompletableDeferred<T>.awaitThen(
    function : (T) -> Unit,
) : T {
    val result : T = this.await()
    kotlin.run { function.invoke(result) }
    return result
}