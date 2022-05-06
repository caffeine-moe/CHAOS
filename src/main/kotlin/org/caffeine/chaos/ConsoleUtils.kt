package org.caffeine.chaos

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun clear() {
    print("\u001b[H\u001b[2J\u001B[38;5;255m")
}

fun log(text: String, prefix: String = "") {
    val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss"))
    if (prefix != "") {
        println("\u001B[38;5;255m[\u001B[38;5;33m${time}\u001B[38;5;255m] $prefix \u001B[38;5;33m$text")
        return
    }
    println("\u001B[38;5;255m[\u001B[38;5;33m${time}\u001B[38;5;255m] CHAOS: \u001B[38;5;33m$text")
}

fun printLogo() {
    println("\u001B[38;5;255m" +
            " ▄████▄   ██░ ██  ▄▄▄       ▒█████    ██████ \n" +
            "▒██▀ ▀█  ▓██░ ██▒▒████▄    ▒██▒  ██▒▒██    ▒ \n" +
            "▒▓█    ▄ ▒██▀▀██░▒██  ▀█▄  ▒██░  ██▒░ ▓██▄   \n" +
            "▒▓▓▄ ▄██▒░▓█ ░██ ░██▄▄▄▄██ ▒██   ██░  ▒   ██▒\n" +
            "▒ ▓███▀ ░░▓█▒░██▓ ▓█   ▓██▒░ ████▓▒░▒██████▒▒\n" +
            "░ ░▒ ▒  ░ ▒ ░░▒░▒ ▒▒   ▓▒█░░ ▒░▒░▒░ ▒ ▒▓▒ ▒ ░\n" +
            "  ░  ▒    ▒ ░▒░ ░  ▒   ▒▒ ░  ░ ▒ ▒░ ░ ░▒  ░ ░\n" +
            "░         ░  ░░ ░  ░   ▒   ░ ░ ░ ▒  ░  ░  ░  \n" +
            "░ ░       ░  ░  ░      ░  ░    ░ ░        ░  \n" +
            "░                                            "
    )
}

fun printSeparator() {
    println("\u001B[38;5;255m─────────────────────────────────────────────")
}