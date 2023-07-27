package org.caffeine.chaos

import kotlinx.coroutines.runBlocking
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.client.on
import org.caffeine.chaos.api.typedefs.ClientType
import org.caffeine.chaos.api.typedefs.LoggerLevel
import org.caffeine.chaos.api.utils.ConsoleColour
import org.caffeine.chaos.api.utils.log
import org.caffeine.chaos.config.Config
import org.caffeine.chaos.handlers.handleArgs
import org.caffeine.chaos.handlers.handleMessage
import org.caffeine.chaos.handlers.handleMessageDelete
import org.caffeine.chaos.handlers.handleReady
import org.caffeine.chaos.processes.loadConfig
import org.caffeine.chaos.utils.clear
import org.caffeine.chaos.utils.printLogo
import org.caffeine.chaos.utils.printSeparator
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

suspend fun init(args : Array<String> = arrayOf()) {
    clear()
    printLogo()
    printSeparator()
    handleArgs(args)
    loadConfig()
}

fun main(args : Array<String> = arrayOf()) = runBlocking {
    init(args)
    log("${ConsoleColour.BLUE.value}CHAOS is starting...", "CHAOS:")
    val client = Client.build {
        config {
            token = config.token
            clientType = ClientType.USER
            loggerLevel = LoggerLevel.ALL
        }
    }

    client.on<ClientEvent.Ready> { handleReady(client) }
    client.on<ClientEvent.MessageCreate> { handleMessage(this, client) }
    client.on<ClientEvent.MessageDelete> { handleMessageDelete(this, client) }

    client.login()
}