package org.caffeine.chaos.api.utils

import org.caffeine.chaos.api.typedefs.LogLevel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

enum class ConsoleColour(val value : String) {
    WHITE("\u001B[38;5;255m"),
    BLUE("\u001B[38;5;33m"),
    GREEN("\u001B[38;5;47m"),
    RED("\u001B[38;5;197m"),
}

// logger utility
fun log(text : String, prefix : String = "", level : LogLevel? = null) {
    // gets current date and time
    val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss.SSS"))
    if (level != null) {
        when {
            level.client.configuration.loggerLevel.ordinal == 3 && level.level.ordinal > 3 -> return
            level.client.configuration.loggerLevel.ordinal == 2 && level.level.ordinal > 2 -> return
            level.client.configuration.loggerLevel.ordinal == 1 && level.level.ordinal > 1 -> return
            level.client.configuration.loggerLevel.ordinal == 0 -> return
        }
    }
    val padLength = (time.length + prefix.length) - 5
    val contentInline = text.replace("\n", "\n".padEnd(padLength, " ".first()))
    println(
        ConsoleColour.WHITE.value +
                "[${ConsoleColour.BLUE.value}" +
                "${time}${ConsoleColour.WHITE.value}] " +
                "${prefix.ifBlank { "OCTANE:" }} " +
                "${ConsoleColour.BLUE.value}$contentInline"
    )
}