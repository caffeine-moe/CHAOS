package org.caffeine.chaos.api.typedefs

enum class ConnectionType {
    CONNECT,
    DISCONNECT,
    RECONNECT,
    RECONNECT_AND_RESUME,
    KILL
}