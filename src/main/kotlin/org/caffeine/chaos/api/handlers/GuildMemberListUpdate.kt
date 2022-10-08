package org.caffeine.chaos.api.handlers

import org.caffeine.chaos.api.client.Client

/*@kotlinx.serialization.Serializable
private data class GuildMemberListUpdate(
    val d : GuildMemberListUpdateD = GuildMemberListUpdateD(),
    val op : Int = 0,
    val s : Int = 0,
    val t : String = "",
)

@kotlinx.serialization.Serializable
private data class GuildMemberListUpdateD(
    val groups : List<Group> = listOf(),
    val guild_id : String = "",
    val id : String = "",
    val member_count : Int = 0,
    val online_count : Int = 0,
    val ops : List<Op> = listOf(),
)

@kotlinx.serialization.Serializable
private data class Group(
    val count : Int = 0,
    val id : String = "",
)

@kotlinx.serialization.Serializable
private data class Op(
    val items : List<Item> = listOf(),
    val op : String = "",
    val range : List<Int> = listOf(),
    val index : Int = 0,
)

@kotlinx.serialization.Serializable
private data class Item(
    val group : Group = Group(),
    val member : GuildMember = GuildMember(),
)

@kotlinx.serialization.Serializable
private data class Presence(
    val activities : List<Activity> = listOf(),
    val client_status : ClientStatus = ClientStatus(),
    val game : String? = "",
    val status : String = "",
    val user : User = User(),
)

@kotlinx.serialization.Serializable
private data class Activity(
    val application_id : String = "",
    val assets : Assets = Assets(),
    val created_at : Long = 0,
    val details : String = "",
    val emoji : Emoji = Emoji(),
    val id : String = "",
    val name : String = "",
    val state : String = "",
    val timestamps : Timestamps = Timestamps(),
    val type : Int = 0,
)

@kotlinx.serialization.Serializable
private data class ClientStatus(
    val desktop : String = "",
    val mobile : String = "",
    val web : String = "",
)

@kotlinx.serialization.Serializable
private data class Assets(
    val large_image : String = "",
    val large_text : String = "",
)

@kotlinx.serialization.Serializable
private data class Timestamps(
    val start : Long = 0,
)*/

fun guildMemberListUpdate(payload : String, client : Client) {
    /*    val decoded = jsonc.decodeFromString<GuildMemberListUpdate>(payload)
        val guild = client.user.guilds.find { it.id == decoded.d.guild_id } ?: return
        for (op in decoded.d.ops) {
            when (op.op) {
                "DELETE" -> {
                    if (guild.members.size >= op.index) {
                        guild.members.removeAt(op.index)
                    }
                }
                "UPDATE" -> {
                    for (i in op.items) {
                        val member = i.member
                        val rep = guild.members.withIndex().find { it.value.user.id == member.user.id } ?: return
                        guild.members[rep.index] = rep.value
                    }
                }
                "SYNC" -> {
                    for (i in op.items) {
                        val member = i.member

                        if (!guild.members.contains(member)) {
                            guild.members.add(member)
                        }
                    }
                }
            }
        }*/
}
