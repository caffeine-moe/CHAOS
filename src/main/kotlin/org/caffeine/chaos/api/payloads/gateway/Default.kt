package org.caffeine.chaos.api.payloads.gateway

@kotlinx.serialization.Serializable
class Default(
    override val op : Int = 0,
    override val s : Int? = 0,
    override val t : String? = "",
) : BasePayload()
