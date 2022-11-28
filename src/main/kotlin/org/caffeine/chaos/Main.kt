package org.caffeine.chaos

import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.asSnowflake
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.json
import org.caffeine.chaos.api.typedefs.ClientType
import org.caffeine.chaos.api.typedefs.LoggerLevel
import org.caffeine.chaos.api.utils.*
import org.caffeine.chaos.config.Config
import java.io.File
import java.net.ConnectException
import kotlin.system.exitProcess

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
}

suspend fun main(args : Array<String> = arrayOf()) = coroutineScope {
    init(args)
    loadConfig()
    checkNetwork()
    val client = Client
        .setClientType(ClientType.USER)
        .setToken(config.token)
        .setLogLevel(LoggerLevel.ALL)
        .build()
    // web ui benched for now
    /*         val ui = WebUI()
        ui.init(client)*/
    // adds listeners

    launch {
        client.events.takeWhile { it != ClientEvent.End }.collect {
            launch {
                when (it) {
                    is ClientEvent.Ready -> {
                        ready(client)
                    }

                    is ClientEvent.MessageCreate -> {
                        handleMessage(it, client)
                    }
                }
            }
        }
    }

    client.login()
}

suspend fun loadConfig() = coroutineScope {

    if (!configFile.exists()) {
        val default = javaClass.classLoader.getResource("defaultconfig.json")
        this.also {
            configFile.createNewFile()
        }
        configFile.writeText(default!!.readText())
        log(
            "Config not found, we have generated one for you at ${configFile.absolutePath}",
            "${ConsoleColour.RED.value}ERROR:"
        )
        log("${ConsoleColour.BLUE.value}Please change the file accordingly. Documentation: https://caffeine.moe/CHAOS/")
        exitProcess(0)
    }
    try {
        config = json.decodeFromString(configFile.readText())
    } catch (e : SerializationException) {
        log(
            "Unable to interpret config, please make sure that the one you have is structured the same as the one here: https://caffeine.moe/CHAOS/config.json",
            "ERROR:",
        )
        log("Full stacktrace here:")
        e.printStackTrace()
        exitProcess(69)
    }
    runConfigTasks()
}

private suspend fun runConfigTasks() {
    if (config.updater.enabled) {
        update()
    }
    if (config.anti_scam.enabled) {
        fetchAntiScam()
    }
}

suspend fun checkNetwork() {
    try {
        normalHTTPClient.get("https://example.com")
    } catch (e : ConnectException) {
        log("Unable to connect to the internet; internet access is needed for CHAOS.")
        exitProcess(69)
    }
}

suspend fun fetchAntiScam() {
    scamLinks =
        json.decodeFromString<AntiScamResponse>(
            normalHTTPClient.get("https://raw.githubusercontent.com/nikolaischunk/discord-phishing-links/main/domain-list.json")
                .bodyAsText()
        ).domains
}
