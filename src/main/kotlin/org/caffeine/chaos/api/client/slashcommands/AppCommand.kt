package org.caffeine.chaos.api.client.slashcommands

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppCommand(
    @SerialName("id")
    val id : String = "",
    @SerialName("application_id")
    val applicationId : String = "",
    @SerialName("version")
    val version : String = "",
    @SerialName("default_permission")
    val defaultPermission : Boolean = false,
    @SerialName("default_member_permissions")
    val defaultMemberPermissions : String? = null,
    @SerialName("type")
    val type : Int = 0,
    @SerialName("name")
    val name : String = "",
    @SerialName("description")
    val description : String = "",
    @SerialName("dm_permission")
    val dmPermission : Boolean = false,
)