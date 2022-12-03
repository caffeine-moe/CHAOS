package org.caffeine.chaos.api.client.connection.payloads.gateway

interface SerialBaseChannel {
    val id : String
    val name : String
    val type : Int
}