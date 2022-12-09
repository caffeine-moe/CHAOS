package org.caffeine.chaos.processes

import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.json
import org.caffeine.chaos.api.utils.ConsoleColour
import org.caffeine.chaos.api.utils.log
import org.caffeine.chaos.api.utils.normalHTTPClient
import org.caffeine.chaos.config
import org.caffeine.chaos.versionDouble
import org.caffeine.chaos.versionString
import java.io.File
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.concurrent.CompletableFuture
import kotlin.system.exitProcess

@Serializable
private data class GitHubResponse(
    @SerialName("assets")
    val assets : List<Asset> = listOf(),
    @SerialName("published_at")
    val publishedAt : String = "",
    @SerialName("tag_name")
    val tagName : String = "",
)

@Serializable
private data class Asset(
    @SerialName("browser_download_url")
    val browserDownloadUrl : String = "",
    @SerialName("content_type")
    val contentType : String = "",
    @SerialName("created_at")
    val createdAt : String = "",
    @SerialName("download_count")
    val downloadCount : Int = 0,
    @SerialName("id")
    val id : Int = 0,
    @SerialName("name")
    val name : String = "",
    @SerialName("node_id")
    val nodeId : String = "",
    @SerialName("size")
    val size : Int = 0,
    @SerialName("state")
    val state : String = "",
    @SerialName("updated_at")
    val updatedAt : String = "",
    @SerialName("url")
    val url : String = "",
)

private data class Updoot(
    val clientIsOutOfDate : Boolean,
    val downUrl : String,
    val latestVerString : String,
    val latestVer : Double,
)

suspend fun checkUpdates() {
    val updateStatus = updateStatus()
    val pre = "UPDATER:"
    if (updateStatus.clientIsOutOfDate) {
        log(
            "Your version of CHAOS is outdated, please update to the latest version. Current version: $versionString, latest version: ${updateStatus.latestVerString}",
            pre
        )
    } else {
        log("${ConsoleColour.GREEN.value}Client is up to date!", pre)
    }
}

suspend fun update() = coroutineScope {
    val updateStatus = updateStatus()
    val pre = "UPDATER:"
    if (updateStatus.clientIsOutOfDate) {
        if (config.updater.notify) {
            log(
                "Your version of CHAOS is outdated, please update to the latest version. Current version: $versionString, latest version: ${updateStatus.latestVerString}",
                pre
            )
            if (!config.updater.auto_download) {
                log("Please visit https://caffeine.moe/CHAOS/ to download the latest version.", pre)
            }
        }
        if (config.updater.auto_download) {
            downloadUpdate(updateStatus.downUrl, updateStatus).thenAccept {
                this.launch {
                    log("Downloaded latest update to $it!", pre)
                }
            }
        }
        if (config.updater.exit) {
            exitProcess(69)
        }
        return@coroutineScope
    }
    log("${ConsoleColour.GREEN.value}Client is up to date!", pre)
}

private suspend fun updateStatus() : Updoot {
    // semver fucking rocks
    val git = git()
    val gnum = git.tagName.replace("[^0-9.]".toRegex(), "").split(".")
    val gmajor = gnum[0]
    val gminor = gnum[1]
    val gpatch = gnum[2]
    val gver = "$gmajor.$gminor$gpatch".toDouble()

    var downUrl = ""
    for (i in git.assets) {
        when (i.contentType) {
            "application/x-java-archive" -> {
                downUrl = i.browserDownloadUrl
            }
        }
    }

    val clientIsOutOfDate = versionDouble != gver
    return Updoot(clientIsOutOfDate, downUrl, gnum.joinToString("."), gver)
}

private suspend fun downloadUpdate(url : String, updoot : Updoot) : CompletableFuture<String> = coroutineScope {
    val filename = "CHAOS-${updoot.latestVerString}.jar"
    run {
        val inputStream = URL(url).openStream()
        Files.copy(
            inputStream,
            Paths.get(filename),
            StandardCopyOption.REPLACE_EXISTING
        )
    }
    val filepath = File("CHAOS-${updoot.latestVerString}.jar").absolutePath
    return@coroutineScope CompletableFuture.completedFuture(filepath)
}

private suspend fun git() : GitHubResponse {
    val response =
        normalHTTPClient.get("https://api.github.com/repos/caffeine-moe/CHAOS/releases/latest")
    return json.decodeFromString(response.bodyAsText())
}
