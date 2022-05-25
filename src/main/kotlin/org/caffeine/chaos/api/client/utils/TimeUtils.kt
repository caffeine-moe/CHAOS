package org.caffeine.chaos.api.client.utils

import kotlin.math.absoluteValue

fun calcNonce(): String {
    val unixTs = System.currentTimeMillis()
    return ((unixTs - 1420070400000) * 4194304).absoluteValue.toString()
}

fun convertIdToUnix(id: String): Long {
    return (id.toLong() / 4194304 + 1420070400000).absoluteValue
}