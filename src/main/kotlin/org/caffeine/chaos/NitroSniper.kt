package org.caffeine.chaos

import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.Dispatchers
import org.caffeine.chaos.api.BASE_URL
import org.caffeine.chaos.api.httpclient
import org.caffeine.chaos.api.client.message.Message
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import java.io.File

suspend fun NitroSniper(event: MessageCreateEvent, config: Config) {
    val giftPrefix = "discord.gift/"
    val rg = giftPrefix + ".{16,24}".toRegex()
    val match = rg.toRegex().matches(event.message.content)
    if (match) {
        val start = System.currentTimeMillis()
        val code = event.message.content.removePrefix(giftPrefix)
        val p = File("Resources")
        val f = File("${p.absolutePath}/tried-nitro-codes.txt")
        if (!p.exists()) {
            p.mkdir()
        }
        if (!f.exists()){
            f.createNewFile()
        }
        if (f.readText().contains(code)){
            return
        }
        f.appendText("$code\n")
        try {
            val response = httpclient.request("$BASE_URL/entitlements/gift-codes/$code/redeem") {
                method = HttpMethod.Post
                headers {
                    append(HttpHeaders.Authorization, config.token)
                }
                expectSuccess = true
            }
        val end = System.currentTimeMillis()
        if (!config.nitro_sniper.silent) {
            val time = start - end
            LogV2("Redeemed code $code! in ${time.toString().replace("-", "")}ms", "NITRO SNIPER:")
        }
        } catch (e: Exception) {
            val end = System.currentTimeMillis()
            val time = start - end
            if (e.toString().contains("Unknown Gift Code")) {
                if (!config.nitro_sniper.silent) {
                    LogV2("Code $code is invalid! (${time.toString().replace("-", "")}ms)", "NITRO SNIPER:")
                }
            }
            if (e.toString().contains("e-mail")) {
                if (!config.nitro_sniper.silent) {
                    LogV2("You need to verify your email in order to use the nitro sniper! (Code: $code) (${time.toString().replace("-", "")}ms)", "NITRO SNIPER:")
                }
            }
        }
    }
}
