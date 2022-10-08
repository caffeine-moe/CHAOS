package org.caffeine.chaos.api.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

enum class ConsoleColours(val value : String) {
    WHITE("\u001B[38;5;255m"),
    BLUE("\u001B[38;5;33m"),
    GREEN("\u001B[38;5;47m"),
    RED("\u001B[38;5;197m")
}

// clears console and sets colour to white
fun clear() {
    print("\u001b[H\u001b[2J${ConsoleColours.WHITE.value}")
}

// logger utility
fun log(text : String, prefix : String = "") {
    // gets current date and time
    val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss.SSS"))
    // prints to console
    println("${ConsoleColours.WHITE.value}[${ConsoleColours.BLUE.value}${time}${ConsoleColours.WHITE.value}] ${prefix.ifBlank { "CHAOS:" }} ${ConsoleColours.BLUE.value}$text")
}

// prints edgy hackerman logo
fun printLogo() {
    println(
        ConsoleColours.WHITE.value +
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
    println("${ConsoleColours.WHITE.value}─────────────────────────────────────────────")
}
