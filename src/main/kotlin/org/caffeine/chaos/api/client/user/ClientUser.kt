package org.caffeine.chaos.api.client.user

import kotlinx.coroutines.CompletableDeferred
import org.caffeine.chaos.api.entities.Snowflake
import org.caffeine.chaos.api.entities.guild.Guild
import org.caffeine.chaos.api.entities.nitro.RedeemedCode
import org.caffeine.chaos.api.entities.users.User
import org.caffeine.chaos.api.typedefs.HypeSquadHouseType
import org.caffeine.chaos.api.typedefs.StatusType
import org.caffeine.chaos.api.typedefs.ThemeType

interface ClientUser : User {

    val verified : Boolean
    val email : String?
    val settings : ClientUserSettings
    val premium : Boolean?

    val relationships : Map<Snowflake, User>
    val friends : Map<Snowflake, User>
    val blocked : Map<Snowflake, User>
    suspend fun removeRelationship(user : User)

    suspend fun setHouse(house : HypeSquadHouseType)

    suspend fun setCustomStatus(status : CustomStatus)
    suspend fun setTheme(theme : ThemeType)

    suspend fun setStatus(status : StatusType)

    suspend fun redeemCode(code : String) : CompletableDeferred<RedeemedCode>

    suspend fun block(user : User)

    fun muteGuild(guild : Guild, i : Int)

}