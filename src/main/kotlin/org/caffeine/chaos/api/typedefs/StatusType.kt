package org.caffeine.chaos.api.typedefs

enum class StatusType(val value : String) {
    ONLINE("online"),
    IDLE("idle"),
    DND("dnd"),
    INVISIBLE("invisible"),
    UNKNOWN("unknown");
}