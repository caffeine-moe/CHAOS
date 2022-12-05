package org.caffeine.chaos.api.utils

import org.caffeine.chaos.api.typedefs.LogLevel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

enum class ConsoleColour(val value : String) {
    WHITE("\u001B[38;5;255m"),
    BLUE("\u001B[38;5;33m"),
    GREEN("\u001B[38;5;47m"),
    RED("\u001B[38;5;197m")
}

// clears console and sets colour to white
fun clear() {
    print("\u001b[H\u001b[2J${ConsoleColour.WHITE.value}")
}

// logger utility
fun log(text : String, prefix : String = "", level : LogLevel? = null) {
    // gets current date and time
    val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss.SSS"))
    when {
        level != null -> {
            when {
                level.client.loggerLevel.ordinal == 3 && level.level.ordinal > 3 -> return
                level.client.loggerLevel.ordinal == 2 && level.level.ordinal > 2 -> return
                level.client.loggerLevel.ordinal == 1 && level.level.ordinal > 1 -> return
                level.client.loggerLevel.ordinal == 0 -> return
            }
        }
    }
    println(
        ConsoleColour.WHITE.value +
                "[${ConsoleColour.BLUE.value}" +
                "${time}${ConsoleColour.WHITE.value}] " +
                "${prefix.ifBlank { "CHAOS:" }} " +
                "${ConsoleColour.BLUE.value}$text"
    )
}

// prints edgy hackerman logo
fun printLogo() {
    println(
        ConsoleColour.WHITE.value +
                " ▄████▄   ██░ ██  ▄▄▄       ▒█████    ██████ \n" +
                "▒██▀ ▀█  ▓██░ ██▒▒████▄    ▒██▒  ██▒▒██    ▒ \n" +
                "▒▓█    ▄ ▒██▀▀██░▒██  ▀█▄  ▒██░  ██▒░ ▓██▄   \n" +
                "▒▓▓▄ ▄██▒░▓█ ░██ ░██▄▄▄▄██ ▒██   ██░  ▒   ██▒\n" +
                "▒ ▓███▀ ░░▓█▒░██▓ ▓█   ▓██▒░ ████▓▒░▒██████▒▒\n" +
                "░ ░▒ ▒  ░ ▒ ░░▒░▒ ▒▒   ▓▒█░░ ▒░▒░▒░ ▒ ▒▓▒ ▒ ░\n" +
                "  ░  ▒    ▒ ░▒░ ░  ▒   ▒▒ ░  ░ ▒ ▒░ ░ ░▒  ░ ░\n" +
                "░         ░  ░░ ░  ░   ▒   ░ ░ ░ ▒  ░  ░  ░  \n" +
                "░ ░       ░  ░  ░      ░  ░    ░ ░        ░  \n" +
                "░                                              "
    )
}

// prints line separator lol
fun printSeparator() {
    println("${ConsoleColour.WHITE.value}─────────────────────────────────────────────")
}
