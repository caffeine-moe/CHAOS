package org.caffeine.chaos.api.client.slashcommands

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Data(
    @SerialName("version")
    val version : String = "",
    @SerialName("id")
    val id : String = "",
    @SerialName("name")
    val name : String = "",
    @SerialName("type")
    val type : Int = 0,
    @SerialName("options")
    val options : Array<String> = emptyArray(),
    @SerialName("application_command")
    val applicationCommand : AppCommand = AppCommand(),
    @SerialName("attachments")
    val attachments : Array<String> = emptyArray(),
)
