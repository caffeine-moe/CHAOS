package org.caffeine.chaos.api.typedefs

enum class StatusType(val value : String) {
    ONLINE("online"),
    IDLE("idle"),
    DND("dnd"),
    INVISIBLE("invisible"),
    UNKNOWN("unknown");

    companion object {
        fun enumById(input : String) : StatusType {
            return enumValues<StatusType>().firstOrNull { it.value == input } ?: UNKNOWN
        }
    }
}