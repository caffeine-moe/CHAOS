package org.caffeine.chaos.api.client.user

import org.caffeine.chaos.api.models.Guild
import org.caffeine.chaos.api.typedefs.StatusType
import org.caffeine.chaos.api.typedefs.ThemeType


data class ClientUserSettings(
    val afkTimeout : Int = 0,
    val allowAccessibilityDetection : Boolean = false,
    val animateEmoji : Boolean = false,
    val animateStickers : Int = 0,
    val contactSyncEnabled : Boolean = false,
    val convertEmoticons : Boolean = false,
    val customStatus : CustomStatus = CustomStatus(),
    val defaultGuildsRestricted : Boolean = false,
    val detectPlatformAccounts : Boolean = false,
    val developerMode : Boolean = false,
    val disableGamesTab : Boolean = false,
    val enableTtsCommand : Boolean = false,
    val explicitContentFilter : Int = 0,
    val friendDiscoveryFlags : Int = 0,
    val gifAutoPlay : Boolean = false,
    val inlineAttachmentMedia : Boolean = false,
    val inlineEmbedMedia : Boolean = false,
    val locale : String = "",
    val messageDisplayCompact : Boolean = false,
    val nativePhoneIntegrationEnabled : Boolean = false,
    val passwordless : Boolean = false,
    val renderEmbeds : Boolean = false,
    val renderReactions : Boolean = false,
    val restrictedGuilds : List<Guild> = listOf(),
    val showCurrentGame : Boolean = false,
    val status : StatusType = StatusType.UNKNOWN,
    val stream_notifications_enabled : Boolean = false,
    val theme : ThemeType = ThemeType.UNKNOWN,
    val timezone_offset : Int = 0,
)