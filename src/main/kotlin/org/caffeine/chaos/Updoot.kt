package org.caffeine.chaos

import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.json
import org.caffeine.chaos.api.normalHTTPClient
import java.io.File
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.concurrent.CompletableFuture

@Serializable
data class GitHubResponse(
    @SerialName("assets")
    val assets: List<Asset> = listOf(),
    @SerialName("assets_url")
    val assetsUrl: String = "",
    @SerialName("body")
    val body: String = "",
    @SerialName("created_at")
    val createdAt: String = "",
    @SerialName("draft")
    val draft: Boolean = false,
    @SerialName("html_url")
    val htmlUrl: String = "",
    @SerialName("id")
    val id: Int = 0,
    @SerialName("mentions_count")
    val mentionsCount: Int = 0,
    @SerialName("name")
    val name: String = "",
    @SerialName("node_id")
    val nodeId: String = "",
    @SerialName("prerelease")
    val prerelease: Boolean = false,
    @SerialName("published_at")
    val publishedAt: String = "",
    @SerialName("tag_name")
    val tagName: String = "",
    @SerialName("tarball_url")
    val tarballUrl: String = "",
    @SerialName("target_commitish")
    val targetCommitish: String = "",
    @SerialName("upload_url")
    val uploadUrl: String = "",
    @SerialName("url")
    val url: String = "",
    @SerialName("zipball_url")
    val zipballUrl: String = "",
)

@Serializable
data class Asset(
    @SerialName("browser_download_url")
    val browserDownloadUrl: String = "",
    @SerialName("content_type")
    val contentType: String = "",
    @SerialName("created_at")
    val createdAt: String = "",
    @SerialName("download_count")
    val downloadCount: Int = 0,
    @SerialName("id")
    val id: Int = 0,
    @SerialName("name")
    val name: String = "",
    @SerialName("node_id")
    val nodeId: String = "",
    @SerialName("size")
    val size: Int = 0,
    @SerialName("state")
    val state: String = "",
    @SerialName("updated_at")
    val updatedAt: String = "",
    @SerialName("url")
    val url: String = "",
)

data class Updoot(
    val clientIsOutOfDate: Boolean,
    val downUrl: String,
    val latestVerString: String,
    val latestVer: Double,
)

suspend fun updateStatus(client: Client): Updoot {

    //semver fucking rocks
    val git = git()
    val gnum = git.tagName.replace("[A-z]".toRegex(), "").split(".")
    val gmajor = gnum[0]
    val gminor = gnum[1]
    val gpatch = gnum[2]
    val gver = if (gnum[3].toIntOrNull() != null && client.config.updater.prereleases) {
        "$gmajor.$gminor$gpatch${gnum[3]}".toDouble()
    } else {
        "$gmajor.$gminor$gpatch".toDouble()
    }

    var downUrl = ""
    for (i in git.assets) {
        when (i.contentType) {
            "application/x-java-archive" -> {
                downUrl = i.browserDownloadUrl
            }
        }
    }

    var clientIsOutOfDate = false

    if (versionDouble != gver) {
        clientIsOutOfDate = true
        if (git.prerelease && !client.config.updater.prereleases) {
            clientIsOutOfDate = false
        }
    }
    return Updoot(clientIsOutOfDate, downUrl, gnum.joinToString("."), gver)
}

suspend fun downloadUpdate(url: String): CompletableFuture<String> {
    val filename = "CHAOS-${versionString}.jar"
    withContext(Dispatchers.IO) {
        val inputStream = URL(url).openStream()
        Files.copy(inputStream,
            Paths.get(filename),
            StandardCopyOption.REPLACE_EXISTING)
    }
    val filepath = File("CHAOS-${versionString}.jar").absolutePath
    return CompletableFuture.completedFuture(filepath)
}

private suspend fun git(): GitHubResponse {
    val response =
        normalHTTPClient.get("https://api.github.com/repos/caffeine-moe/CHAOS/releases/latest")
    return json.decodeFromString(response.bodyAsText())
}