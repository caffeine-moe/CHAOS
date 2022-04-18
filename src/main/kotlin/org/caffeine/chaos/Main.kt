package org.caffeine.chaos

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.caffeine.chaos.api.client.Client
import java.io.File
import java.net.URL
import kotlin.system.exitProcess

//version lmao
const val version: Float = 1.0F

//config
@Serializable
data class Config(
    val token: String,
    val prefix: String,
    val log_commands: Boolean,
    val auto_delete: auto_delete,
    val exchange: exchange,
    val backup: backup,
    val restore: restore,
    val nitro_sniper: nitro_sniper,
)

@Serializable
data class auto_delete(
    val user: Boolean,
    val bot: bot,
    val user_delay: Long,
    val bot_delay: Long
)

@Serializable
data class bot(
    val enabled: Boolean,
    val content_generation: Boolean
)

@Serializable
data class exchange(
    val base: String,
    val target: String
)

@Serializable
data class backup(
    val servers: Boolean,
    val dmed_users: Boolean,
    val friends: Boolean,
    val block_list: Boolean
)

@Serializable
data class restore(
    val dmed_users: Boolean,
    val friends: Boolean,
    val block_list: Boolean
)

@Serializable
data class nitro_sniper(
    val enabled: Boolean,
    val silent: Boolean
)

suspend fun main(): Unit = coroutineScope{
    clear()
    printLogo()
    printSeparator()
    if (!File("config.json").exists()) {
        val default = URL("https://caffeine.moe/CHAOS/config.json").readText(Charsets.UTF_8).trim()
        withContext(Dispatchers.IO) {
            File("config.json").createNewFile()
        }
        File("config.json").writeText(default)
        log(
            "Config not found, we have generated one for you at ${File("config.json").absolutePath}",
            "\u001B[38;5;197mERROR:"
        )
        log("\u001B[38;5;33mPlease change the file accordingly. Documentation: https://caffeine.moe/CHAOS/")
        exitProcess(0)
    }
    log("\u001B[38;5;33mInitialising gateway connection...")
    val config = Json.decodeFromString<Config>(File("config.json").readText())
    val client = Client()
    launch{ client.login(config, client) }.start()
}
