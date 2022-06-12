package org.caffeine.chaos.config

@kotlinx.serialization.Serializable
data class YTDL (
    val download: Boolean,
    val upload: Boolean,
    )