package org.caffeine.chaos.api.payloads.gateway

@kotlinx.serialization.Serializable
class Default(
    val op : Int = 0,
    val s : Int? = 0,
    val t : String? = "",
)