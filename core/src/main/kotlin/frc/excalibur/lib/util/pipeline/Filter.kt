package frc.excalibur.lib.util.pipeline

/**
 * Returns a default value if
 */
class Filter<K>(
    /** true to retain, false to remove*/
    private val predicate: (K) -> Boolean,
    private val default: K
) : PipelineStage<K, K> {

    constructor(eliminateValue: K, default: K) : this({ it != eliminateValue }, default)

    override fun apply(value: K): K = if (predicate(value)) value else default
}