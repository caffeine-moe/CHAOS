package org.caffeine.chaos.api.client.utils


fun webkitBoundary(): String {
    val charset = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789"
    val random = (1..16)
        .map { charset.random() }
        .joinToString("")
    return "WebKitFormBoundary$random"
}