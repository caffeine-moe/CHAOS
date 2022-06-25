package org.caffeine.chaos.api

enum class ConnectionType {
    CONNECT,
    DISCONNECT,
    RECONNECT,
    RECONNECT_AND_RESUME,
}