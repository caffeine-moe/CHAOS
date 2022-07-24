package org.caffeine.chaos.api.handlers

import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.client.ClientImpl
import org.caffeine.chaos.api.jsonc
import org.caffeine.chaos.api.models.Guild

@kotlinx.serialization.Serializable
private data class GuildCreate(
    val d : GuildCreateD = GuildCreateD(),
    val op : Int = 0,
    val s : Int = 0,
    val t : String = "",
)

@kotlinx.serialization.Serializable
data class GuildCreateD(
    val afk_channel_id : String = "",
    val afk_timeout : Int = 0,
    val banner : String = "",
    //val channels : MutableList<ClientGuildChannel> = mutableListOf(),
    val default_message_notifications : Int = 0,
    val description : String = "",
    val discovery_splash : String = "",
    val emojis : List<Emoji> = listOf(),
    val explicit_content_filter : Int = 0,
    val features : List<String> = listOf(),
    val hub_type : String? = "",
    val icon : String = "",
    val id : String = "",
    val joined_at : String = "",
    val large : Boolean = false,
    val lazy : Boolean = false,
    val max_members : Int = 0,
    val max_video_channel_users : Int = 0,
    val member_count : Int = 0,
    //val members : MutableList<ClientGuildMember> = mutableListOf(),
    val mfa_level : Int = 0,
    val name : String = "",
    val nsfw : Boolean = false,
    val nsfw_level : Int = 0,
    val owner_id : String = "",
    val preferred_locale : String = "",
    val premium_progress_bar_enabled : Boolean = false,
    val premium_subscription_count : Int = 0,
    val premium_tier : Int = 0,
    val public_updates_channel_id : String = "",
    val region : String = "",
    val roles : List<Role> = listOf(),
    val rules_channel_id : String = "",
    val splash : String = "",
    val system_channel_flags : Int = 0,
    val system_channel_id : String = "",
    val vanity_url_code : String = "",
    val verification_level : Int = 0,
)

@kotlinx.serialization.Serializable
data class Emoji(
    val animated : Boolean = false,
    val available : Boolean = false,
    val id : String = "",
    val managed : Boolean = false,
    val name : String = "",
    val require_colons : Boolean = false,
    val roles : List<Role> = listOf(),
)

@kotlinx.serialization.Serializable
data class Role(
    val color : Int = 0,
    val flags : Int = 0,
    val hoist : Boolean = false,
    val icon : String? = "",
    val id : String = "",
    val managed : Boolean = false,
    val mentionable : Boolean = false,
    val name : String = "",
    val permissions : Int = 0,
    val permissions_new : String = "",
    val position : Int = 0,
    val unicode_emoji : String? = "",
)

fun guildCreate(payload : String, client : ClientImpl) {
        val parsed = jsonc.decodeFromString<GuildCreate>(payload)
        val guild = Guild(
            parsed.d.id,
            parsed.d.name,
            parsed.d.icon,
            parsed.d.description,
            parsed.d.splash,
        )
        client.userImpl
}