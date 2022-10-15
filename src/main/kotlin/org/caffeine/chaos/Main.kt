package org.caffeine.chaos

import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.client.ClientEvents
import org.caffeine.chaos.api.client.ClientFactory
import org.caffeine.chaos.api.json
import org.caffeine.chaos.api.typedefs.ClientType
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
    log("${ConsoleColours.BLUE.value}CHAOS is starting...")
}

// main function
suspend fun main(args : Array<String> = arrayOf()) = coroutineScope {
    // init
    init(args)
    // load config
    loadConfig()
    // checks if internet access is available
    checkNetwork()
    // gets antiscam links
    // makes new client
    val client = ClientFactory()
        .setClientType(ClientType.USER)
        .setToken(config.token)
        .build()
    // web ui benched for now
    /*         val ui = WebUI()
        ui.init(client)*/
    // adds listeners
    launch {
        client.events.collect {
            launch {
                when (it) {
                    is ClientEvents.Ready -> {
                        ready(client)
                    }

                    is ClientEvents.MessageCreate -> {
                        handleMessage(it, client)
                    }
                }
            }
        }
    }

    client.login()
}

suspend fun loadConfig() = coroutineScope {
    // checks if config exists, if not, create one and exit
    if (!configFile.exists()) {
        val default = javaClass.classLoader.getResource("defaultconfig.json")
        this.also {
            configFile.createNewFile()
        }
        configFile.writeText(default!!.readText())
        log(
            "Config not found, we have generated one for you at ${configFile.absolutePath}",
            "${ConsoleColours.RED.value}ERROR:"
        )
        log("${ConsoleColours.BLUE.value}Please change the file accordingly. Documentation: https://caffeine.moe/CHAOS/")
        exitProcess(0)
    }
    // tries to read config
    try {
        config = json.decodeFromString(configFile.readText())
    } catch (e : Exception) {
        // if it cant read the config then it logs that its invalid
        if (e.toString().contains("JsonDecodingException")) {
            e.printStackTrace()
            log(
                "Unable to interpret config, please make sure that the one you have is structured the same as the one here: https://caffeine.moe/CHAOS/config.json",
                "\u001B[38;5;197mERROR:"
            )
            log("Full stacktrace here:")
            e.printStackTrace()
            exitProcess(69)
        }
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
