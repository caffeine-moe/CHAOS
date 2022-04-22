package org.caffeine.chaos.api.client.message

data class MessageFilters(
    var mentioning_user_id: String?,
    var author_id: String?,
    var before_id: Long?,
    var after_id: Long?,
    var limit: Int?,
)
