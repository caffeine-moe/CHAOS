package org.caffeine.chaos.api.models.message

data class MessageSearchFilters(
    var mentioning_user_id : String = "",
    var author_id : String = "",
    var before_id : String = "",
    var after_id : String = "",
)