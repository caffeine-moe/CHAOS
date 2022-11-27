package org.caffeine.chaos.api.entities.message

import org.caffeine.chaos.api.Snowflake

data class MessageSearchFilters(
    var mentioning_user_id : Snowflake = Snowflake(""),
    var author_id : Snowflake = Snowflake(""),
    var before_id : Snowflake = Snowflake(""),
    var after_id : Snowflake = Snowflake(""),
)
