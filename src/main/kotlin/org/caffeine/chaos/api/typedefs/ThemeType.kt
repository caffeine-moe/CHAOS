package org.caffeine.chaos.api.typedefs

enum class ThemeType(val value : String) {
    DARK("dark"),
    LIGHT("light"),
    UNKNOWN("unknown");

    companion object {
        fun enumById(input : String) : ThemeType {
            return enumValues<ThemeType>().firstOrNull { it.value == input } ?: UNKNOWN
        }
    }
}