package org.caffeine.chaos.api.entities.guild

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.entities.Snowflake
import org.caffeine.chaos.api.entities.channels.GuildChannel

interface Guild {
    val id : Snowflake
    val name : String
    val icon : String
    val description : String?
    val splash : String?
    val discoverySplash : String?
    val banner : String?
    val ownerId : Snowflake
    val applicationId : Snowflake?
    val region : String
    val afkChannelId : Snowflake?
    val afkTimeout : Int
    val systemChannelId : Snowflake?
    val widgetEnabled : Boolean
    val widgetChannelId : Snowflake?
    val verificationLevel : Int
    val defaultMessageNotifications : Int
    val mfaLevel : Int
    val explicitContentFilter : Int
    val maxPresences : Int
    val maxMembers : Int
    val maxVideoChannelUsers : Int
    val vanityUrlCode : String?
    val premiumTier : Int
    val premiumSubscriptionCount : Int
    val systemChannelFlags : Int
    val preferredLocale : String
    val rulesChannelId : Snowflake?
    val publicUpdatesChannelId : Snowflake?
    val embedEnabled : Boolean
    val embedChannelId : Snowflake
    val emojis : Map<Snowflake, Emoji>
    val client : Client

    val members : Map<Snowflake, GuildMemberData>
        get() = client.user.guildMembers.filterValues { it.guild.id == id }

    val channels : Map<Snowflake, GuildChannel>
        get() = client.user.guildChannels.filterValues { it.guild.id == id }

    val vanityUrl : String?

    fun muteForever()
}