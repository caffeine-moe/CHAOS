package org.caffeine.chaos.api.entities.guild

import kotlinx.coroutines.runBlocking
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.entities.Snowflake
import org.caffeine.chaos.api.entities.users.UserImpl
import org.caffeine.chaos.api.typedefs.PermissionType
import org.caffeine.chaos.api.typedefs.RelationshipType

data class GuildMember(
    val guildMember: GuildMemberData?,
    val guild : Guild,
    override val username : String = "",
    override val discriminator : String = "",
    override val avatar : String? = "",
    override val id : Snowflake = Snowflake(0),
    override val relation : RelationshipType,
    override val bot : Boolean = false,
    override val client : Client,
) : UserImpl(username, discriminator, avatar, id, relation, bot, client) {

    var guildId = Snowflake(0)

    val roles : Map<Snowflake, Role>
        get() = client.user.guildRoles.filterValues { this.guildMemberData.roleIds.contains(it.id) }  // TODO: optimize loll

    val permissions : List<PermissionType> get() = roles.values.flatMap { it.permissions }.distinct()

    private val guildMemberData get() = runBlocking(client.coroutineContext) { client.user.fetchGuildMember(this@GuildMember, guild) }

    val nickname get() = this.guildMemberData.nickname
    val joinedAt get() = this.guildMemberData.joinedAt
    val premiumSince get() = this.guildMemberData.premiumSince
    val deafened get() = this.guildMemberData.deafened
    val muted get() = this.guildMemberData.muted
}