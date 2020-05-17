package frc.excalibur.lib.util.pipeline

inline class Bounds(val range: ClosedFloatingPointRange<Double>) {
    constructor(min: Double, max: Double) : this(min..max)

    fun apply(value: Double): Double = when (value) {
        in range -> value
        in Double.MIN_VALUE..range.start -> range.start
        in range.endInclusive..Double.MAX_VALUE -> range.endInclusive
        else -> value
    }

}