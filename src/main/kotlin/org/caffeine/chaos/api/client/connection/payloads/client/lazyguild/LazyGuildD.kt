package org.caffeine.chaos.api.client.connection.payloads.client.lazyguild

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

@Serializable
data class LazyGuildD(
    val guild_id : String = "",
    val typing : Boolean = true,
    val threads : Boolean = false,
    val activities : Boolean = true,
    val members : Array<String> = arrayOf(),
    val channels : JsonObject = JsonObject(mutableMapOf(Pair("", JsonPrimitive(1)))),
)
