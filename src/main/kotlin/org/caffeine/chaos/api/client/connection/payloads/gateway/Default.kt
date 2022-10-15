package org.caffeine.chaos.api.client.connection.payloads.gateway

@kotlinx.serialization.Serializable
class Default(
    override val op : Int = 0,
    override val s : Int? = 0,
    override val t : String? = "",
) : BasePayload()
