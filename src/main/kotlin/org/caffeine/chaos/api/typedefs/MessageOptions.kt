package org.caffeine.chaos.api.typedefs

interface MessageOptions {
    val content: String?
    val tts: Boolean?
    //val embed: MessageEmbed;
}

interface MessageDeleteOptions {
    val timeout: Number?;
    val reason: String?;
}