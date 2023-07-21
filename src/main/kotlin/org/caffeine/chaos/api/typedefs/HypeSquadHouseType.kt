package org.caffeine.chaos.api.typedefs

enum class HypeSquadHouseType(val value : String) {
    NONE("none"),
    BRAVERY("bravery"),
    BRILLIANCE("brilliance"),
    BALANCE("balance"),
    UNKNOWN("unknown");

    companion object {
        fun enumById(input : String) : HypeSquadHouseType {
            return enumValues<HypeSquadHouseType>().firstOrNull { it.value == input } ?: UNKNOWN
        }
    }
}