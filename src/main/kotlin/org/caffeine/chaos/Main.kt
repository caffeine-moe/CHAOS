package org.caffeine.chaos

import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvents
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.api.json
import org.caffeine.chaos.api.utils.*
import org.caffeine.chaos.config.Config
import java.io.File
import kotlin.system.exitProcess

//version lmao
const val versionString : String = "3.0.0"
const val versionDouble : Double = 3.00

//gets unix time in ms when program starts
val programStartedTime = System.currentTimeMillis()

//scam link list
var scamLinks = listOf<String>()

lateinit var config : Config

var configFile = File("config.json")

//main function
suspend fun main(args : Array<String> = arrayOf()) : Unit = coroutineScope {
    //init
    clear()
    printLogo()
    printSeparator()
    handleArgs(args)
    log("${ConsoleColours.BLUE.value}CHAOS is starting...")
    //checks if config exists, if not, create one and exit
    if (!configFile.exists()) {
        val default = javaClass.classLoader.getResource("defaultconfig.json")
        withContext(Dispatchers.IO) {
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
    //tries to read config
    try {
        config = json.decodeFromString(configFile.readText())
    } catch (e : Exception) {
        //if it cant read the config then it logs that its invalid
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
    //gets antiscam links
    if (config.anti_scam.enabled) {
        scamLinks =
            json.decodeFromString<AntiScamResponse>(normalHTTPClient.get("https://raw.githubusercontent.com/nikolaischunk/discord-phishing-links/main/domain-list.json")
                .bodyAsText()).domains
    }
    //makes new client
    val client = Client()
    //web ui benched for now
/*         val ui = WebUI()
        ui.init(client)*/
    //checks if client is up to date
    if (config.updater.enabled) {
        update()
    }

    //adds listeners
    launch {
        client.events.collect {
            launch {
                if (it is ClientEvents.Ready) {
                    ready(client)
                }
                if (it is MessageCreateEvent) {
                    handleMessage(it, client)
                }
            }
        }
    }

    //logs in
    client.login(config.token)
}
