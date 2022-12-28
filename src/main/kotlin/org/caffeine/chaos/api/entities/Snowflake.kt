package org.caffeine.chaos.api.entities

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.TimeMark


/**
 * Thanks kord!
 */


@Serializable(with = Snowflake.Serializer::class)
class Snowflake : Comparable<Snowflake> {

    val value : ULong

    constructor(value : ULong) {
        this.value = value.coerceIn(validValues)
    }

    constructor(value : String) : this(value.toULong())

    constructor(timestamp : Instant) : this(
        timestamp.toEpochMilliseconds()
            .coerceAtLeast(DISCORD_EPOCH_LONG) // time before is unknown to Snowflakes
            .minus(DISCORD_EPOCH_LONG)
            .toULong()
            .coerceAtMost(maxMillisecondsSinceDiscordEpoch) // time after is unknown to Snowflakes
            .shl(TIMESTAMP_SHIFT)
    )

    private inline val millisecondsSinceDiscordEpoch get() = value shr TIMESTAMP_SHIFT

    val timestamp : Instant
        get() = Instant.fromEpochMilliseconds(DISCORD_EPOCH_LONG + millisecondsSinceDiscordEpoch.toLong())

    @OptIn(ExperimentalTime::class)
    val timeMark : TimeMark
        get() = SnowflakeTimeMark(timestamp)

    val workerId : UByte
        get() = value.and(WORKER_MASK).shr(WORKER_SHIFT).toUByte()

    val processId : UByte
        get() = value.and(PROCESS_MASK).shr(PROCESS_SHIFT).toUByte()

    val increment : UShort
        get() = value.and(INCREMENT_MASK).toUShort()

    operator fun component1() : Instant = timestamp

    operator fun component2() : UByte = workerId

    operator fun component3() : UByte = processId

    operator fun component4() : UShort = increment


    override fun compareTo(other : Snowflake) : Int =
        millisecondsSinceDiscordEpoch.compareTo(other.millisecondsSinceDiscordEpoch)

    override fun toString() : String = value.toString()

    fun isZero() : Boolean = value.toInt() == 0

    fun toLong() : Long = value.toLong()

    override fun hashCode() : Int = value.hashCode()

    override fun equals(other : Any?) : Boolean = other is Snowflake && this.value == other.value


    companion object {
        private const val DISCORD_EPOCH_LONG = 1420070400000L

        private const val TIMESTAMP_SHIFT = 22

        private const val WORKER_MASK = 0x3E0000uL
        private const val WORKER_SHIFT = 17

        private const val PROCESS_MASK = 0x1F000uL
        private const val PROCESS_SHIFT = 12

        private const val INCREMENT_MASK = 0xFFFuL


        val validValues : ULongRange = ULong.MIN_VALUE..Long.MAX_VALUE.toULong() // 0..9223372036854775807

        val min : Snowflake = Snowflake(validValues.first)

        val max : Snowflake = Snowflake(validValues.last)

        val discordEpoch : Instant = Instant.fromEpochMilliseconds(DISCORD_EPOCH_LONG)

        val endOfTime : Instant = max.timestamp

        private val maxMillisecondsSinceDiscordEpoch = max.millisecondsSinceDiscordEpoch
    }

    internal object Serializer : KSerializer<Snowflake> {
        override val descriptor : SerialDescriptor = ULong.serializer().descriptor

        override fun deserialize(decoder : Decoder) : Snowflake =
            Snowflake(decoder.decodeInline(descriptor).decodeLong().toULong())

        override fun serialize(encoder : Encoder, value : Snowflake) {
            encoder.encodeInline(descriptor).encodeLong(value.value.toLong())
        }
    }
}

fun String.asSnowflake() = Snowflake(this.ifBlank { 0 }.toString())

@OptIn(ExperimentalTime::class)
private class SnowflakeTimeMark(private val timestamp : Instant) : TimeMark {

    override fun elapsedNow() : Duration = Clock.System.now() - timestamp
}

fun Snowflake(value : Long) : Snowflake = Snowflake(value.coerceAtLeast(0).toULong())