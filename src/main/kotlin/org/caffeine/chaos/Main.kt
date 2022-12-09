package org.caffeine.chaos

import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.json
import org.caffeine.chaos.api.typedefs.ClientType
import org.caffeine.chaos.api.typedefs.LoggerLevel
import org.caffeine.chaos.api.utils.*
import org.caffeine.chaos.config.Config
import org.caffeine.chaos.processes.AntiScamResponse
import org.caffeine.chaos.processes.update
import java.io.File
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

suspend fun loadConfig() = coroutineScope {

    val prefix = "ERROR:"

    if (!configFile.exists()) {
        val default = javaClass.classLoader.getResource("defaultconfig.json") ?: run {
            log(
                "Config not found, and we were unable to generate one for you, please create a config file and put this in it https://caffeine.moe/CHAOS/config.json",
                prefix
            ); return@coroutineScope
        }
        this.also {
            configFile.createNewFile()
        }
        configFile.writeText(default.readText())
        log(
            "Config not found, we have generated one for you at ${configFile.absolutePath}",
            prefix
        )
        log("Please change the file accordingly. Documentation: https://caffeine.moe/CHAOS/")
        exitProcess(0)
    }
    try {
        config = json.decodeFromString(configFile.readText())
    } catch (e : SerializationException) {
        log(
            "Unable to interpret config, please make sure that the one you have is structured the same as the one here: https://caffeine.moe/CHAOS/config.json",
            prefix
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

suspend fun fetchAntiScam() {
    scamLinks =
        json.decodeFromString<AntiScamResponse>(
            normalHTTPClient.get("https://raw.githubusercontent.com/nikolaischunk/discord-phishing-links/main/domain-list.json")
                .bodyAsText()
        ).domains
}
