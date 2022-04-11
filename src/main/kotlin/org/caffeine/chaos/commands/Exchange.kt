package org.caffeine.chaos.commands

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.caffeine.chaos.Config
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.concurrent.thread

@Serializable
data class exchange(
    val ticker: ticker,
)

@Serializable
data class ticker(
    val base: String,
    val target: String,
    val price: Float,
    val change: Float?,
)

/*
fun Exchange(client: DiscordApi, event: MessageCreateEvent, config: Config) {
    thread {
        val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yy hh:mm:ss"))
        if (event.messageContent.lowercase() == ("${config.prefix}exchange") || event.messageContent.lowercase() == ("${config.prefix}crypto")) {
            val basec = config.exchange.base.uppercase()
            val targetc = config.exchange.target.uppercase()
            val url = "https://api.cryptonator.com/api/ticker/$basec-$targetc"
            val response = URL(url).readText()
            val parsedresponse = Json { ignoreUnknownKeys = true }.decodeFromString<main>(response)
            event.channel.sendMessage(
                ":money_with_wings: Exchange data for $basec to $targetc\nPrice in $targetc: ${parsedresponse.ticker.price} $targetc\nMarket change (24h): ${parsedresponse.ticker.change}%"
            ).thenAccept { message -> }
        }
        if (event.messageContent.lowercase()
                .startsWith("${config.prefix}exchange") && event.messageContent.lowercase() != ("${config.prefix}exchange") || event.messageContent.lowercase()
                .startsWith("${config.prefix}crypto") && event.messageContent.lowercase() != ("${config.prefix}crypto")
        ) {
            try {
                val msg = event.messageContent.lowercase().split(" ")
                if (!msg[1].toFloat().isNaN()) {
                    val number = msg[1].toFloat()
                    val base = msg[2].uppercase()
                    val target = msg[3].uppercase()
                    val url = "https://api.cryptonator.com/api/ticker/$base-$target"
                    val response = URL(url).readText()
                    val parsedresponse = Json { ignoreUnknownKeys = true }.decodeFromString<main>(response)
                    val price = parsedresponse.ticker.price * number
                    event.channel.sendMessage(
                        ":money_with_wings: Exchange data for $number $base to $target\n$number $base in $target: $price $target\nMarket change (24h): ${parsedresponse.ticker.change}%\n$number $base in $target: $price $target"
                    ).thenAccept { message -> }

                }
                val base = msg[1].uppercase()
                val target = msg[2].uppercase()
                val url = "https://api.cryptonator.com/api/ticker/$base-$target"
                val response = URL(url).readText()
                val parsedresponse = Json.decodeFromString<main>(response)
                event.channel.sendMessage(
                    ":money_with_wings: Exchange data for $base to $target\nPrice in $target: ${parsedresponse.ticker.price} $target"
                ).thenAccept { message -> }
            } catch (e: Exception) {
                when (e) {
                    is IndexOutOfBoundsException -> {
                        event.channel.sendMessage(
                            "Incorrect usage '${event.messageContent}'\nError: Not enough parameters!\nCorrect usage: `${config.prefix}exchange Currency(String) Currency(String)`"
                        ).thenAccept { message -> }
                    }
                    is NumberFormatException -> {
                        event.channel.sendMessage(
                            "Incorrect usage '${event.messageContent}'\nError: ${
                                event.messageContent.lowercase().split(" ")[3]
                            } is not a valid number!\nCorrect usage: `${config.prefix}exchange Amount(Float/Int) Currency(String) Currency(String)`",
                        ).thenAccept { message -> }
                    }
                    else -> {
                        println(e)
                    }
                }
            }
        }
    }
}*/
