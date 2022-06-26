package org.caffeine.chaos.api.client.message

data class MessageFilters(
    var mentioning_user_id : String = "",
    var author_id : String = "",
    var before_id : String = "",
    var after_id : String = "",
    var limit : Int = 0,
    var needed : Int = 0,
)