package org.caffeine.chaos.api.typedefs

enum class ChannelType(val id : Int) {
    TEXT(0),
    DM(1),
    VOICE(2),
    GROUP(3),
    CATEGORY(4),
    ANNOUNCEMENT(5),
    ANNOUNCEMENT_THREAD(10),
    PUBLIC_THREAD(11),
    PRIVATE_THREAD(12),
    STAGE(13),
    DIRECTORY(14),
    FORUM(15),
    UNKNOWN(-1);

    companion object {
        fun enumById(input : Int) : ChannelType {
            return enumValues<ChannelType>().firstOrNull { it.id == input } ?: UNKNOWN
        }
    }
}
