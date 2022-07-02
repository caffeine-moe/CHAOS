package org.caffeine.chaos.api.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

enum class ConsoleColours(val value : String) {
    WHITE("\u001B[38;5;255m"),
    BLUE("\u001B[38;5;33m"),
    GREEN("\u001B[38;5;47m"),
    RED("\u001B[38;5;197m")
}

//clears console and sets colour to white
fun clear() {
    print("\u001b[H\u001b[2J${ConsoleColours.WHITE.value}")
}

//logger utility
fun log(text : String, prefix : String = "") {
    //gets current date and time
    var message : String
    val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss"))

    //if the prefix is blank set the message with the default prefix
    message =
        "${ConsoleColours.WHITE.value}[${ConsoleColours.BLUE.value}${time}${ConsoleColours.WHITE.value}] CHAOS: ${ConsoleColours.BLUE.value}$text"

    //if the prefix parameter is not blank then set the message with the specified prefix
    if (prefix.isNotBlank()) {
        message =
            "${ConsoleColours.WHITE.value}[${ConsoleColours.BLUE.value}${time}${ConsoleColours.WHITE.value}] $prefix ${ConsoleColours.BLUE.value}$text"
    }

    println(message)
}

//prints edgy hackerman logo
fun printLogo() {
    println(ConsoleColours.WHITE.value +
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

//prints line separator lol
fun printSeparator() {
    println("${ConsoleColours.WHITE.value}─────────────────────────────────────────────")
}