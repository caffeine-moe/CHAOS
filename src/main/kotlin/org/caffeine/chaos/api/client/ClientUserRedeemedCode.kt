package org.caffeine.chaos.api.client

data class ClientUserRedeemedCode(
    var code: String = "",
    var latency: Long = -1,
    var status: ClientUserRedeemedCodeStatus = ClientUserRedeemedCodeStatus.INVALID,
    var error: ClientUserRedeemedCodeError = ClientUserRedeemedCodeError.NONE,
)
