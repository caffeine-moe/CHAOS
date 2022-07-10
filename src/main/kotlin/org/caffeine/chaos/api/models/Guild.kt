package org.caffeine.chaos.api.models

data class Guild(
    val id: String = "",
    val name: String = "",
    val icon: String = "",
    val description: String? = null,
    val splash: String? = null,
    val discoverySplash: String? = null,
    val features: Array<Any> = arrayOf(),
    val banner: String? = null,
    val ownerId: String = "",
    val applicationId: String = "",
    val region: String = "",
    val afkChannelId: String = "",
    val afkTimeout: String = "",
    val systemChannelId: String = "",
    val widgetEnabled: Boolean = false,
    val widgetChannelId: String = "",
    val verificationLevel: Number = 0,
    val defaultMessageNotifications: Number = 0,
    val mfaLevel: Number = 0,
    val explicitContentFilter: Number = 0,
    val maxPresences: Number = 0,
    val maxMembers: Number = 0,
    val maxVideoChannelUsers: Number = 0,
    val vanityUrl: String? = null,
    val premiumTier: Number = 0,
    val premiumSubscriptionCount: Number = 0,
    val systemChannelFlags: Number = 0,
    val preferredLocale: String = "",
    val rulesChannelId: String = "",
    val publicUpdatesChannelId: String = "",
    val embedEnabled: Boolean = false,
    val embedChannelId: String = "",
)