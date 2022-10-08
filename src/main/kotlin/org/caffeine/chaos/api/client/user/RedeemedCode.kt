package org.caffeine.chaos.api.client.user

import org.caffeine.chaos.api.typedefs.RedeemedCodeErrorType
import org.caffeine.chaos.api.typedefs.RedeemedCodeStatusType

data class RedeemedCode(
    var code : String = "",
    var latency : Long = -1,
    var status : RedeemedCodeStatusType = RedeemedCodeStatusType.INVALID,
    var error : RedeemedCodeErrorType = RedeemedCodeErrorType.NONE,
)
