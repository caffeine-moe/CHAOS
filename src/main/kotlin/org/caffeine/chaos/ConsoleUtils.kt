package org.caffeine.chaos

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun Log(text: String) {
    val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yy hh:mm:ss"))
    println("\u001B[38;5;255m[\u001B[38;5;33m${time}\u001B[38;5;255m] $text")
}

fun LogV2(text: String, prefix: String) {
    val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yy hh:mm:ss"))
    println("\u001B[38;5;255m[\u001B[38;5;33m${time}\u001B[38;5;255m] $prefix \u001B[38;5;33m$text")
}