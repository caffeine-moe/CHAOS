package org.caffeine.chaos.processes

import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.json
import org.caffeine.chaos.api.utils.log
import org.caffeine.chaos.config
import org.caffeine.chaos.configFile
import kotlin.system.exitProcess

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
    if (config.updater.enabled) {
        update()
    }
    if (config.anti_scam.enabled) {
        fetchAntiScam()
    }
}