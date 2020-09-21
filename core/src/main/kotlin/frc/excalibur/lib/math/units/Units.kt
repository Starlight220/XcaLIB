package frc.excalibur.lib.math.units

import kotlin.math.PI

inline class SIUnit<T : SIKey>(val value: Double) : Comparable<SIUnit<T>> {
    override operator fun compareTo(other: SIUnit<T>): Int = value.compareTo(other.value)
    operator fun plus(other: SIUnit<T>): SIUnit<T> =
        SIUnit(this.value + other.value)

    operator fun minus(other: SIUnit<T>): SIUnit<T> =
        SIUnit(this.value - other.value)

    operator fun <K : SIKey> div(other: SIUnit<K>): SIUnit<Ratio<T, K>> =
        SIUnit(this.value / other.value)

    operator fun <K : SIKey> times(other: SIUnit<K>): SIUnit<Product<T, K>> =
        SIUnit(this.value * other.value)
}

object Abstract : SIKey
object Second : SIKey
object Meter : SIKey
object Degree : SIKey
object Volt : SIKey
object Ampere : SIKey

class Ratio<K1 : SIKey, K2 : SIKey> :
    SIKey

class Product<K1 : SIKey, K2 : SIKey> :
    SIKey

typealias Squared<K> = Product<K, K>
typealias Invert<K> = Ratio<Abstract, K>

fun Double.meters(): SIUnit<Meter> =
    SIUnit<Meter>(this)

fun Double.centimeters(): SIUnit<Meter> =
    SIUnit<Meter>(100.0 * this)

fun Double.millimeters(): SIUnit<Meter> = (10 * this).centimeters()

fun Double.milliseconds(): SIUnit<Second> =
    SIUnit<Second>(this / 1000)

fun Double.seconds(): SIUnit<Second> =
    SIUnit<Second>(this)

fun Double.minutes(): SIUnit<Second> =
    SIUnit<Second>(60.0 * this)

fun Double.degrees(): SIUnit<Degree> =
    SIUnit<Degree>(this)

fun Double.radians(): SIUnit<Degree> =
    SIUnit<Degree>(180.0 / PI * this)

fun Double.volts(): SIUnit<Volt> =
    SIUnit<Volt>(this)

fun Double.amperes(): SIUnit<Ampere> =
    SIUnit<Ampere>(this)

fun Double.milliamperes(): SIUnit<Ampere> =
    SIUnit<Ampere>(0.001 * this)

interface SIKey
