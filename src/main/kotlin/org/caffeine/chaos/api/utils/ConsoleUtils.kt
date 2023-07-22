package org.caffeine.chaos.api.utils

import org.caffeine.chaos.api.typedefs.LogLevel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

enum class ConsoleColour(val value : String) {
    WHITE("\u001B[38;5;255m"),
    BLUE("\u001B[38;5;33m"),
    GREEN("\u001B[38;5;47m"),
    RED("\u001B[38;5;197m"),
}

// clears console and sets colour to white
fun clear() {
    print("\u001b[H\u001b[2J${ConsoleColour.WHITE.value}")
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
                "${prefix.ifBlank { "CHAOS:" }} " +
                "${ConsoleColour.BLUE.value}$contentInline"
    )
}

fun christmasLogo() {
    println(
        ConsoleColour.GREEN.value +
                "8                                   o    o                     8      8             8      \n" +
                "8                                   8    8                     8      8             8      \n" +
                "8oPYo. .oPYo. .oPYo. .oPYo. o    o o8oooo8 .oPYo. odYo. o    o 8  .o  8  .o  .oPYo. 8oPYo. \n" +
                "8    8 .oooo8 8    8 8    8 8    8  8    8 .oooo8 8' `8 8    8 8oP'   8oP'   .oooo8 8    8 \n" +
                "8    8 8    8 8    8 8    8 8    8  8    8 8    8 8   8 8    8 8 `b.  8 `b.  8    8 8    8 \n" +
                "8    8 `YooP8 8YooP' 8YooP' `YooP8  8    8 `YooP8 8   8 `YooP' 8  `o. 8  `o. `YooP8 8    8 \n" +
                "..:::..:.....:8 ....:8 ....::....8 :..:::..:.....:..::..:.....:..::.....::...:.....:..:::..\n" +
                "::::::::::::::8 :::::8 :::::::ooP'.::::::::::::::::::::::::::::::::::::::::::::::::::::::::\n" +
                "::::::::::::::..:::::..:::::::...::::::::::::::::::::::::::::::::::::::::::::::::::::::::::\n"
    )
}


// prints edgy hackerman logo
fun printLogo() {
    if (LocalDate.now().monthValue == 12 && LocalDate.now().dayOfMonth == 25) {
        christmasLogo(); return
    }
    println(
        ConsoleColour.WHITE.value +
                "░█████╗░██╗░░██╗░█████╗░░█████╗░░██████╗\n" +
                "██╔══██╗██║░░██║██╔══██╗██╔══██╗██╔════╝\n" +
                "██║░░╚═╝███████║███████║██║░░██║╚█████╗░\n" +
                "██║░░██╗██╔══██║██╔══██║██║░░██║░╚═══██╗\n" +
                "╚█████╔╝██║░░██║██║░░██║╚█████╔╝██████╔╝\n" +
                "░╚════╝░╚═╝░░╚═╝╚═╝░░╚═╝░╚════╝░╚═════╝░"
    )
}

// prints line separator lol
fun printSeparator() {
    println("${ConsoleColour.WHITE.value}─────────────────────────────────────────────")
}