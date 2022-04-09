package org.caffeine.chaos

import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.caffeine.chaos.api.BASE_URL
import org.caffeine.chaos.api.D
import org.caffeine.chaos.api.httpclient
import org.javacord.api.event.message.MessageCreateEvent
import java.io.File

fun NitroSniper(event: D, config: Config) {
    val giftPrefix = "discord.gift/"
    val rg = giftPrefix + ".{16,24}".toRegex()
    val match = rg.toRegex().matches(event.content.toString())
    if (match) {
        val start = System.currentTimeMillis()
        val code = event.content.toString().removePrefix(giftPrefix)
        val p = File("Resources")
        if (!p.exists())
            p.mkdir()
        val f = File("${p.absolutePath}/tried-nitro-codes.txt")
        if (!f.exists()) f.createNewFile()
        if (f.readText().contains(code)) return
        f.appendText("$code\n")
        try {
            runBlocking {
                httpclient.request<String>("$BASE_URL/entitlements/gift-codes/$code/redeem") {
                    method = HttpMethod.Post
                    headers {
                        append(HttpHeaders.Authorization, config.token)
                    }
                }
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