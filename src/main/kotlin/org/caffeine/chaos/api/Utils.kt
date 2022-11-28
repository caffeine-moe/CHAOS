package org.caffeine.chaos.api

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import org.caffeine.chaos.api.utils.MessageBuilder
import org.caffeine.chaos.api.utils.MessageData
import org.caffeine.chaos.api.utils.convertIdToUnix

internal const val GATEWAY = "gateway.discord.gg"
internal const val BASE_URL = "https://discord.com/api/v9"

fun String.asSnowflake() : Snowflake = createSnowflake(this)
fun createSnowflake(id : String) = Snowflake(id)

@JvmInline
value class Snowflake(private val s : String) {
    private fun String.isValidSnowflake() : Boolean {
        if (s.isBlank()) return true
        val unix = convertIdToUnix(this)
        return unix <= System.currentTimeMillis() && unix > 1420070400000
    }

    init {
        require(s.isValidSnowflake())
    }

    fun asString() = s

    fun asUnixTs() : Long = convertIdToUnix(this.asString())
}

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
