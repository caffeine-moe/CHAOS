package org.caffeine.chaos.api.discord.client

@kotlinx.serialization.Serializable
data class ClientGuildMember(
    val avatar : String? = "",
    val communication_disabled_until : String? = "",
    val deaf : Boolean = false,
    val flags : Int = 0,
    val joined_at : String = "",
    val mute : Boolean = false,
    val nick : String? = "",
    val pending : Boolean = false,
    val premium_since : String? = "",
    val roles : List<String> = listOf(),
    val user : ClientGuildMemberUser = ClientGuildMemberUser(),
)