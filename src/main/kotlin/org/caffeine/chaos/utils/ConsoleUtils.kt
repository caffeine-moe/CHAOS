package org.caffeine.chaos.utils

import org.caffeine.octane.utils.ConsoleColour
import java.time.LocalDate

// clears console and sets colour to white
fun clear() {
    print("\u001b[H\u001b[2J${ConsoleColour.WHITE.value}")
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
    println("${ConsoleColour.WHITE.value}────────────────────────────────────────")
}