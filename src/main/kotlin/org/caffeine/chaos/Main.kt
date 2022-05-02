package org.caffeine.chaos

import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.httpclient
import org.caffeine.chaos.config.Config
import java.io.File
import kotlin.system.exitProcess

//version lmao
const val version: Float = 1.0F

suspend fun main(): Unit = coroutineScope {
    clear()
    printLogo()
    printSeparator()
    log("\u001B[38;5;33mCHAOS is starting...")
    if (!File("config.json").exists()) {
        val default = httpclient.get("https://caffeine.moe/CHAOS/config.json")
        withContext(Dispatchers.IO) {
            File("config.json").createNewFile()
        }
        File("config.json").writeText(default.bodyAsText(Charsets.UTF_8))
        log(
            "Config not found, we have generated one for you at ${File("config.json").absolutePath}",
            "\u001B[38;5;197mERROR:"
        )
        log("\u001B[38;5;33mPlease change the file accordingly. Documentation: https://caffeine.moe/CHAOS/")
        exitProcess(0)
    }
    log("\u001B[38;5;33mInitialising gateway connection...")
    val config = Json.decodeFromString<Config>(File("config.json").readText())
    val client = Client(config)
    client.login(config)
}
