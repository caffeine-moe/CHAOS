package org.caffeine.chaos.api.entities.message

import org.caffeine.chaos.api.Snowflake

data class MessageFilters(
    var mentioningUserId : Snowflake = Snowflake(""),
    var authorId : Snowflake = Snowflake(""),
    var beforeId : Snowflake = Snowflake(""),
    var afterId : Snowflake = Snowflake(""),
    var limit : Int = 0,
    var needed : Int = 0,
)
