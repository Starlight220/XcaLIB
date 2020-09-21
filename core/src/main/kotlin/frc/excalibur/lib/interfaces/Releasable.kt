package frc.excalibur.lib.interfaces

/**
 * Defines the [-][unaryMinus] operator for shutdown/neutralization purposes.
 */
interface Releasable {
    /**
     * Neutralize this object.
     */
    fun release()

    /**
     * Neutralize this object.
     */
    operator fun unaryMinus(): Unit = release()

    /**
     * A useful implementation of [Releasable] to delegate from if the class is given a release
     * lambda.
     */
    class ReleaseLambda(private inline val releaser: () -> Unit) : Releasable {
        override fun release(): Unit = releaser()
    }
}
