package org.caffeine.chaos.api.typedefs

enum class MessageType(val id : Int) {
    DEFAULT(0),
    RECIPIENT_ADD(1),
    RECIPIENT_REMOVE(2),
    CALL(3),
    CHANNEL_NAME_CHANGE(4),
    CHANNEL_ICON_CHANGE(5),
    CHANNEL_PINNED_MESSAGE(6),
    USER_JOIN(7),
    GUILD_BOOST(8),
    GUILD_BOOST_TIER_1(9),
    GUILD_BOOST_TIER_2(10),
    GUILD_BOOST_TIER_3(11),
    CHANNEL_FOLLOW_ADD(12),
    GUILD_DISCOVERY_DISQUALIFIED(14),
    GUILD_DISCOVERY_REQUALIFIED(15),
    GUILD_DISCOVERY_GRACE_PERIOD_INITIAL_WARNING(16),
    GUILD_DISCOVERY_GRACE_PERIOD_FINAL_WARNING(17),
    THREAD_CREATED(18),
    REPLY(19),
    CHAT_INPUT_COMMAND(20),
    THREAD_STARTER_MESSAGE(21),
    GUILD_INVITE_REMINDER(22),
    CONTEXT_MENU_COMMAND(23),
    AUTO_MODERATION_ACTION(24),
    UNKNOWN(-1);

    companion object {
        fun enumById(input: Int): MessageType {
            return enumValues<MessageType>().firstOrNull { it.id == input } ?: UNKNOWN
        }
    }
}