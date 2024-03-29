package org.caffeine.chaos

import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.json
import org.caffeine.chaos.api.normalHTTPClient
import org.caffeine.chaos.api.scamLinks
import org.caffeine.chaos.config.Config
import java.io.File
import kotlin.system.exitProcess

//version lmao
const val versionString : String = "2.2.2"
const val versionDouble : Double = 2.22

//gets unix time in ms when program starts
val programStartedTime = System.currentTimeMillis()

//main function
suspend fun main() : Unit = coroutineScope {
    //init
    clear()
    printLogo()
    printSeparator()
    log("\u001B[38;5;33mCHAOS is starting...")
    //checks if config exists, if not, create one and exit
    val cfgName = "config.json"
    if (!File(cfgName).exists()) {
        val default = javaClass.classLoader.getResource("defaultconfig.json")
        withContext(Dispatchers.IO) {
            File(cfgName).createNewFile()
        }
        File(cfgName).writeText(default!!.readText())
        log(
            "Config not found, we have generated one for you at ${File(cfgName).absolutePath}",
            "\u001B[38;5;197mERROR:"
        )
        log("\u001B[38;5;33mPlease change the file accordingly. Documentation: https://caffeine.moe/CHAOS/")
        exitProcess(0)
    }
    try {
        //tries to read config
        val config : Config = Json.decodeFromString(File(cfgName).readText())
        //gets antiscam links
        if (config.anti_scam.enabled) {
            scamLinks =
                json.decodeFromString<AntiScamResponse>(normalHTTPClient.get("https://raw.githubusercontent.com/nikolaischunk/discord-phishing-links/main/domain-list.json")
                    .bodyAsText()).domains
        }
        //makes new client, and logs in
        val client = Client(config)
        //web ui benched for now
        /* val ui = WebUI()
        launch { ui.init(client) } */
        //checks if client is up to date
        if (client.config.updater.enabled) {
            update(client)
        }
        client.login(config)
    } catch (e : Exception) {
        //if it cant read the config then it logs that its invalid
        if (e.toString().contains("JsonDecodingException")) {
            log(
                "Unable to interpret config, please make sure that the one you have is structured the same as the one here: https://caffeine.moe/CHAOS/config.json",
                "\u001B[38;5;197mERROR:"
            )
            log("Full stacktrace here:")
            e.printStackTrace()
            exitProcess(69)
        }
    }
}
