package org.caffeine.chaos.commands
/*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.caffeine.chaos.Config
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.api.httpclient

@Serializable
private data class cryrsp(
    val error: String,
    val success: Boolean,
    val ticker: Ticker,
    val timestamp: Int,
)

@Serializable
private data class Ticker(
    val base: String,
    val change: String,
    val price: Float,
    val target: String,
    val volume: String,
)

suspend fun Exchange(client: Client, event: MessageCreateEvent, config: Config) = coroutineScope {
    if (event.message.content.lowercase() == ("${config.prefix}exchange") || event.message.content.lowercase() == ("${config.prefix}crypto")) {
        val basec = config.exchange.base.uppercase()
        val targetc = config.exchange.target.uppercase()
        val url = "https://api.cryptonator.com/api/ticker/$basec-$targetc"
        val response = httpclient.request<String>(url) {
            method = HttpMethod.Get
        }
        val parsedresponse = Json { ignoreUnknownKeys = true }.decodeFromString<cryrsp>(response)
        when (parsedresponse.success) {
            true -> {
                event.message.channel.sendMessage(MessageBuilder()
                    .appendLine(":money_with_wings: Exchange data for $basec to $targetc")
                    .appendLine("Price in $targetc: ${parsedresponse.ticker.price} $targetc")
                    .appendLine("Market change (24h): ${parsedresponse.ticker.change}%")
                    .build(), config, client)
                    .thenAccept { message -> this.launch { bot(message, config) } }
            }
            false -> {
                event.message.channel.sendMessage(MessageBuilder()
                    .appendLine("Error exchanging $basec to $targetc")
                    .appendLine(parsedresponse.error)
                    .build(), config, client)
                    .thenAccept { message -> this.launch { bot(message, config) } }
            }
        }
    }
    if (event.message.content.lowercase()
            .startsWith("${config.prefix}exchange") && event.message.content.lowercase() != ("${config.prefix}exchange") || event.message.content.lowercase()
            .startsWith("${config.prefix}crypto") && event.message.content.lowercase() != ("${config.prefix}crypto")
    ) {
        try {
            val msg = event.message.content.lowercase().split(" ")
            if (!msg[1].toFloat().isNaN()) {
                val number = msg[1].toFloat()
                val base = msg[2].uppercase()
                val target = msg[3].uppercase()
                val url = "https://api.cryptonator.com/api/ticker/$base-$target"
                val response = httpclient.request<String>(url) {
                    method = HttpMethod.Get
                }
                val parsedresponse = Json { ignoreUnknownKeys = true }.decodeFromString<cryrsp>(response)
                val price = parsedresponse.ticker.price * number
                when (parsedresponse.success) {
                    true -> {
                        event.message.channel.sendMessage(MessageBuilder()
                            .appendLine(":money_with_wings: Exchange data for $number $base to $target")
                            .appendLine("$number $base in $target: $price $target")
                            .appendLine("Market change (24h): ${parsedresponse.ticker.change}%")
                            .build(), config, client)
                            .thenAccept { message -> this.launch { bot(message, config) } }
                    }
                    false -> {
                        event.message.channel.sendMessage(MessageBuilder()
                            .appendLine("Error exchanging $number $base to $target")
                            .appendLine(parsedresponse.error)
                            .build(), config, client)
                            .thenAccept { message -> this.launch { bot(message, config) } }
                    }
                }
            }
            val base = msg[1].uppercase()
            val target = msg[2].uppercase()
            val url = "https://api.cryptonator.com/api/ticker/$base-$target"
            val response = httpclient.request<String>(url) {
                method = HttpMethod.Get
            }
            val parsedresponse = Json { ignoreUnknownKeys = true }.decodeFromString<cryrsp>(response)
            when (parsedresponse.success) {
                true -> {
                    event.message.channel.sendMessage(MessageBuilder()
                        .appendLine(":money_with_wings: Exchange data for $base to $target")
                        .appendLine("Price in $target: ${parsedresponse.ticker.price} $target")
                        .appendLine("Market change (24h): ${parsedresponse.ticker.change}%")
                        .build(), config, client)
                        .thenAccept { message -> this.launch { bot(message, config) } }
                }
                false -> {
                    event.message.channel.sendMessage(MessageBuilder()
                        .appendLine("Error exchanging $base to $target")
                        .appendLine(parsedresponse.error)
                        .build(), config, client)
                        .thenAccept { message -> this.launch { bot(message, config) } }
                }
            }
        } catch (e: Exception) {
            when (e) {
                is IndexOutOfBoundsException -> {
                    event.message.channel.sendMessage(MessageBuilder()
                        .appendLine("Incorrect usage '${event.message.content}'")
                        .appendLine("Error: Not enough parameters!")
                        .appendLine("Correct usage: `${config.prefix}exchange Currency(String) Currency(String)`")
                        .build(), config, client)
                        .thenAccept { message -> this.launch { bot(message, config) } }
                }
                is NumberFormatException -> {
                    event.message.channel.sendMessage(MessageBuilder()
                        .appendLine("Incorrect usage '${event.message.content}'")
                        .appendLine("Error: ${event.message.content.lowercase().split(" ")[3]} is not a valid number!")
                        .appendLine("Correct usage: `${config.prefix}exchange Amount(Float/Int) Currency(String) Currency(String)`")
                        .build(), config, client)
                        .thenAccept { message -> this.launch { bot(message, config) } }
                }
                else -> {
                    println(e)
                }
            }
        }
    }
}
*/