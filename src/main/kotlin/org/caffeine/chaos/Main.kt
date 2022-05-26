package org.caffeine.chaos

import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.json
import org.caffeine.chaos.api.normalHTTPClient
import org.caffeine.chaos.api.scamLinks
import org.caffeine.chaos.config.Config
import org.caffeine.chaos.ui.WebUI
import java.io.File
import kotlin.system.exitProcess

//version lmao
const val version: Float = 2.0F

//gets time in ms when program starts
val programStartedTime = System.currentTimeMillis()

//main function
suspend fun main(): Unit = coroutineScope {
    //init
    val ui = WebUI()
    clear()
    printLogo()
    printSeparator()
    log("\u001B[38;5;33mCHAOS is starting...")
    //checks if config exists, if not, create one and exit
    if (!File("config.json").exists()) {
        val default = javaClass.classLoader.getResource("defaultconfig.json")
        withContext(Dispatchers.IO) {
            File("config.json").createNewFile()
        }
        File("config.json").writeText(default!!.readText())
        log(
            "Config not found, we have generated one for you at ${File("config.json").absolutePath}",
            "\u001B[38;5;197mERROR:"
        )
        log("\u001B[38;5;33mPlease change the file accordingly. Documentation: https://caffeine.moe/CHAOS/")
        exitProcess(0)
    }
    try {
        //tries to read config
        val config = Json.decodeFromString<Config>(File("config.json").readText())
        //gets antiscam links
        if (config.anti_scam.enabled) {
            scamLinks =
                json.decodeFromString<AntiScamResponse>(normalHTTPClient.get("https://raw.githubusercontent.com/nikolaischunk/discord-phishing-links/main/domain-list.json")
                    .bodyAsText()).domains
        }
        //makes new client and logs in
        val client = Client(config)
        //launches the webui init
        launch {ui.init(client)}
        client.login(config)
    } catch (e: Exception) {
        //if it cant read the config then it logs that its invalid
        if (e.toString().contains("JsonDecodingException")) {
            log(
                "Unable to interpret config, please make sure that the one you have is structured the same as the one here: https://caffeine.moe/CHAOS/config.json",
                "\u001B[38;5;197mERROR:"
            )
            exitProcess(69)
        }
    }
}