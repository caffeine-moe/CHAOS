package org.caffeine.chaos.api.client.user

import org.caffeine.chaos.api.client.ClientImpl

interface BaseClientUserImpl : BaseClientUser {
    val clientImpl : ClientImpl
}