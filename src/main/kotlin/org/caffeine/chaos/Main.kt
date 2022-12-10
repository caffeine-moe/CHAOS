package org.caffeine.chaos

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.typedefs.ClientType
import org.caffeine.chaos.api.typedefs.LoggerLevel
import org.caffeine.chaos.api.utils.*
import org.caffeine.chaos.config.Config
import org.caffeine.chaos.handlers.handleArgs
import org.caffeine.chaos.handlers.handleEvent
import org.caffeine.chaos.processes.loadConfig
import java.io.File

// version lmao
const val versionString : String = "3.0.0"
const val versionDouble : Double = 3.00

// gets unix time in ms when program starts
val programStartedTime = System.currentTimeMillis()

// scam link list
var scamLinks = listOf<String>()

lateinit var config : Config

var configFile = File("config.json")

private suspend fun init(args : Array<String> = arrayOf()) {
    clear()
    printLogo()
    printSeparator()
    handleArgs(args)
    log("${ConsoleColour.BLUE.value}CHAOS is starting...")
    loadConfig()
}

fun main(args : Array<String> = arrayOf()) = runBlocking {
    init(args)
    val client = Client
        .setClientType(ClientType.USER)
        .setToken(config.token)
        .setLogLevel(LoggerLevel.ALL)
        .build()

    launch(Dispatchers.Default) {
        client.events.takeWhile { it != ClientEvent.End }.collect {
            launch { handleEvent(client, it) }
        }
    }

    client.login()
}
