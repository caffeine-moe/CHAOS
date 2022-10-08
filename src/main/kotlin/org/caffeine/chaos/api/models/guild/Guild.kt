package org.caffeine.chaos.api.models.guild

import org.caffeine.chaos.api.client.Client

data class Guild(
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
    val verificationLevel : Number = 0,
    val defaultMessageNotifications : Number = 0,
    val mfaLevel : Number = 0,
    val explicitContentFilter : Number = 0,
    val maxPresences : Number = 0,
    val maxMembers : Number = 0,
    val maxVideoChannelUsers : Number = 0,
    val vanityUrlCode : String? = null,
    val premiumTier : Number = 0,
    val premiumSubscriptionCount : Number = 0,
    val systemChannelFlags : Number = 0,
    val preferredLocale : String = "",
    val rulesChannelId : String? = "",
    val publicUpdatesChannelId : String? = "",
    val embedEnabled : Boolean = false,
    val embedChannelId : String = "",
    val client : Client,
) {

    val vanityUrl : String? = if (vanityUrlCode != null) "https://discord.gg/$vanityUrlCode" else null

    fun muteForever() {
        client.user.muteGuild(this, -1)
    }
}
