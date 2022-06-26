package org.caffeine.chaos.commands

/*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.caffeine.chaos.api.discord.client.Client
import org.caffeine.chaos.api.discord.message.MessageBuilder
import org.caffeine.chaos.api.discord.message.MessageCreateEvent
import org.caffeine.chaos.api.httpclient

@Serializable
private data class CryRsp(
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

suspend fun exchange(client: Client, event: MessageCreateEvent) = coroutineScope {
    if (event.message.content.lowercase() == ("${client.config.prefix}exchange") || event.message.content.lowercase() == ("${client.config.prefix}crypto")) {
        val basec = client.config.exchange.base.uppercase()
        val targetc = client.config.exchange.target.uppercase()
        val url = "https://api.cryptonator.com/api/ticker/$basec-$targetc"
        val response = HttpClient(CIO).request(url) {
            method = HttpMethod.Get
        }
        val parsedresponse = Json { ignoreUnknownKeys = true }.decodeFromString<CryRsp>(response.bodyAsText())
        when (parsedresponse.success) {
            true -> {
                event.channel.sendMessage(MessageBuilder()
                    .appendLine(":money_with_wings: Exchange data for $basec to $targetc")
                    .appendLine("Price in $targetc: ${parsedresponse.ticker.price} $targetc")
                    .appendLine("Market change (24h): ${parsedresponse.ticker.change}%")
                    .build(), client)
                    .thenAccept { message -> this.launch {  onComplete(message, client) } }
            }
            false -> {
                event.channel.sendMessage(MessageBuilder()
                    .appendLine("Error exchanging $basec to $targetc")
                    .appendLine(parsedresponse.error)
                    .build(), client)
                    .thenAccept { message -> this.launch {  onComplete(message, client) } }
            }
        }
    }
    if (event.message.content.lowercase()
            .startsWith("${client.config.prefix}exchange") && event.message.content.lowercase() != ("${client.config.prefix}exchange") || event.message.content.lowercase()
            .startsWith("${client.config.prefix}crypto") && event.message.content.lowercase() != ("${client.config.prefix}crypto")
    ) {
        try {
            val msg = event.message.content.lowercase().split(" ")
            if (!msg[1].toFloat().isNaN()) {
                val number = msg[1].toFloat()
                val base = msg[2].uppercase()
                val target = msg[3].uppercase()
                val url = "https://api.cryptonator.com/api/ticker/$base-$target"
                val response = HttpClient(CIO).request(url) {
                    method = HttpMethod.Get
                }
                val parsedresponse = Json { ignoreUnknownKeys = true }.decodeFromString<CryRsp>(response.bodyAsText())
                val price = parsedresponse.ticker.price * number
                when (parsedresponse.success) {
                    true -> {
                        event.channel.sendMessage(MessageBuilder()
                            .appendLine(":money_with_wings: Exchange data for $number $base to $target")
                            .appendLine("$number $base in $target: $price $target")
                            .appendLine("Market change (24h): ${parsedresponse.ticker.change}%")
                            .build(), client)
                            .thenAccept { message -> this.launch {  onComplete(message, client) } }
                    }
                    false -> {
                        event.channel.sendMessage(MessageBuilder()
                            .appendLine("Error exchanging $number $base to $target")
                            .appendLine(parsedresponse.error)
                            .build(), client)
                            .thenAccept { message -> this.launch {  onComplete(message, client) } }
                    }
                }
            }
            val base = msg[1].uppercase()
            val target = msg[2].uppercase()
            val url = "https://api.cryptonator.com/api/ticker/$base-$target"
            val response = HttpClient(CIO).request(url) {
                method = HttpMethod.Get
            }
            val parsedresponse = Json { ignoreUnknownKeys = true }.decodeFromString<CryRsp>(response.bodyAsText())
            when (parsedresponse.success) {
                true -> {
                    event.channel.sendMessage(MessageBuilder()
                        .appendLine(":money_with_wings: Exchange data for $base to $target")
                        .appendLine("Price in $target: ${parsedresponse.ticker.price} $target")
                        .appendLine("Market change (24h): ${parsedresponse.ticker.change}%")
                        .build(), client)
                        .thenAccept { message -> this.launch {  onComplete(message, client) } }
                }
                false -> {
                    event.channel.sendMessage(MessageBuilder()
                        .appendLine("Error exchanging $base to $target")
                        .appendLine(parsedresponse.error)
                        .build(), client)
                        .thenAccept { message -> this.launch {  onComplete(message, client) } }
                }
            }
        } catch (e: Exception) {
            when (e) {
                is IndexOutOfBoundsException -> {
                    event.channel.sendMessage(MessageBuilder()
                        .appendLine("Incorrect usage '${event.message.content}'")
                        .appendLine("Error: Not enough parameters!")
                        .appendLine("Correct usage: `${client.config.prefix}exchange Currency(String) Currency(String)`")
                        .build(), client)
                        .thenAccept { message -> this.launch {  onComplete(message, client) } }
                }
                is NumberFormatException -> {
                    event.channel.sendMessage(MessageBuilder()
                        .appendLine("Incorrect usage '${event.message.content}'")
                        .appendLine("Error: ${event.message.content.lowercase().split(" ")[3]} is not a valid number!")
                        .appendLine("Correct usage: `${client.config.prefix}exchange Amount(Float/Int) Currency(String) Currency(String)`")
                        .build(), client)
                        .thenAccept { message -> this.launch {  onComplete(message, client) } }
                }
                else -> {
                    println(e)
                }
            }
        }
    }
}
*/