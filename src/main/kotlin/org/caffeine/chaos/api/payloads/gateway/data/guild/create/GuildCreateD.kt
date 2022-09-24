package org.caffeine.chaos.api.payloads.gateway.data.guild.create

import kotlinx.serialization.Serializable

@Serializable
data class GuildCreateD (
    val id : String = "",
    val name : String = "",
    val icon : String = "",
    val description : String? = null,
    val splash : String? = null,
    val discoverySplash : String? = null,
    val banner : String? = null,
    val ownerId : String = "",
    val applicationId : String? = null,
    val region : String = "",
    val afkChannelId : String = "",
    val afkTimeout : Int = 0,
    val systemChannelId : String? = "",
    val widgetEnabled : Boolean = false,
    val widgetChannelId : String = "",
    val verificationLevel : Int = 0,
    val defaultMessageNotifications : Int = 0,
    val mfaLevel : Int = 0,
    val explicitContentFilter : Int = 0,
    val maxPresences : Int = 0,
    val maxMembers : Int = 0,
    val maxVideoChannelUsers : Int = 0,
    val vanityUrl : String? = null,
    val vanityUrlCode : String? = null,
    val premiumTier : Int = 0,
    val premiumSubscriptionCount : Int = 0,
    val systemChannelFlags : Int = 0,
    val preferredLocale : String = "",
    val rulesChannelId : String? = "",
    val publicUpdatesChannelId : String? = "",
    val embedEnabled : Boolean = false,
    val embedChannelId : String = "",
    val channels : List<GuildCreateDChannel> = listOf()
)