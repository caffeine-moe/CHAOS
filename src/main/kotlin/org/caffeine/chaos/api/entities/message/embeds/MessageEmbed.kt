package org.caffeine.chaos.api.entities.message.embeds

import kotlinx.datetime.Instant
import org.caffeine.chaos.api.typedefs.EmbedType

interface MessageEmbed {
    val title : String
    val type : EmbedType
    val description : String
    val url : String
    val timestamp : Instant
    val colour : Int
    val footer : EmbedFooter
    val image : EmbedImage
    val thumbnail : EmbedThumbnail
    val video : EmbedVideo
    val provider : EmbedProvider
    val author : EmbedAuthor
    val fields : List<EmbedField>
}