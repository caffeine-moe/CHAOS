package org.caffeine.chaos.api.client.connection.handlers

import org.caffeine.chaos.api.client.ClientImpl
import org.caffeine.chaos.api.client.connection.payloads.gateway.SerialBaseChannel
import org.caffeine.chaos.api.client.connection.payloads.gateway.SerialGuildChannel
import org.caffeine.chaos.api.client.connection.payloads.gateway.SerialPrivateChannel
import org.caffeine.chaos.api.entities.asSnowflake
import org.caffeine.chaos.api.entities.guild.GuildImpl
import org.caffeine.chaos.api.typedefs.ChannelType
import org.caffeine.chaos.api.utils.tryDecodeFromString

fun channelMod(payload : String, client : ClientImpl) {
    val base = tryDecodeFromString<SerialBaseChannel>(payload) ?: return
    val channel = when (ChannelType.enumById(base.type)) {

        ChannelType.TEXT, ChannelType.CATEGORY -> {
            println("mod")
            val c = tryDecodeFromString<SerialGuildChannel>(payload) ?: return
            val g = client.user.guildChannels[c.parent_id.asSnowflake()]?.guild ?: return
            client.utils.createGuildChannel(c, g as GuildImpl)
        }

        ChannelType.DM -> {
            println(payload)
            client.utils.createDMChannel(tryDecodeFromString<SerialPrivateChannel>(payload) ?: return)
        }

        else -> {
            println(base.type)
            return
        }
    }
    print("created guild channel ${channel.name}")
    client.userImpl.channels[channel.id] = channel
}