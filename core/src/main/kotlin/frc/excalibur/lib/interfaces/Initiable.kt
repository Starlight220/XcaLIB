package frc.excalibur.lib.interfaces

/**
 * Defines the empty [()][invoke] operator, for initialization purposes.
 */
interface Initiable {
    /**
     * Initialize.
     */
    fun init()

    /**
     * Initialize.
     */
    operator fun invoke(): Unit = this.init()
}
