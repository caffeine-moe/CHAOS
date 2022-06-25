package org.caffeine.chaos.api

enum class Event(val value : String) {
    READY("READY"),
    MESSAGE_CREATE("MESSAGE_CREATE"),
    GUILD_DELETE("GUILD_DELETE"),
    GUILD_CREATE("GUILD_CREATE"),
    GUILD_MEMBER_LIST_UPDATE("GUILD_MEMBER_LIST_UPDATE"),
}