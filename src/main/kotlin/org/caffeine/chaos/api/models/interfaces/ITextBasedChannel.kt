package org.caffeine.chaos.api.models.interfaces

import org.caffeine.chaos.api.typedefs.MessageOptions

interface TextBasedChannel {
    suspend fun sendMessage(payload: MessageOptions): Any;
}