package frc.excalibur.lib.devices.can

import edu.wpi.first.wpilibj.Compressor
import edu.wpi.first.wpilibj.DoubleSolenoid
import edu.wpi.first.wpilibj.Solenoid


/**
 * A class representing a [CTRE Pneumatic Control Module](http://www.ctr-electronics.com/pcm.html).
 * Can be used to create [Solenoid] and [DoubleSolenoid] easily with the [DoubleSolenoid]
 */
typealias PCM = PneumaticsControlModule

/**
 * A class representing a [CTRE Pneumatic Control Module](http://www.ctr-electronics.com/pcm.html).
 * Can be used to create [Solenoid][edu.wpi.first.wpilibj.Solenoid] and [doubleSolenoid]
 * [edu.wpi.first.wpilibj.DoubleSolenoid] objects easily with the [singleSolenoid] and
 * [doubleSolenoid] functions.
 */
class PneumaticsControlModule private constructor(private val canID: Int = 0) {
    init {
        instances[canID] = this
    }

    /**
     * The compressor.
     */
    val compressor: Compressor by lazy { Compressor(canID) }

    /**
     * The built-in pressure switch connected to the PCM.
     */
    val pressureSwitch: Boolean
        get() = compressor.pressureSwitchValue


    companion object {
        private val instances: MutableMap<Int, PCM> = mutableMapOf()

        /**
         * Get a PCM instance with the given [canID].
         */
        operator fun invoke(canID: Int = 0): PCM = instances.getOrPut(canID) { PCM(canID) }
    }

    /**
     * Easily create a [DoubleSolenoid] object on this PCM with the given [forward][fwd] and
     * [reverse][rev] ports.
     */
    fun doubleSolenoid(fwd: Int, rev: Int): DoubleSolenoid = DoubleSolenoid(canID, fwd, rev)

    /**
     * Easily create a [Solenoid] object on this PCM with the given [port].
     */
    fun singleSolenoid(port: Int): Solenoid = Solenoid(canID, port)
}

/**
 * A boolean state property for [DoubleSolenoid] objects. Can be more convenient than using
 * [DoubleSolenoid.Value] enum constants.
 */
var DoubleSolenoid.state: Boolean
    get() = get() == DoubleSolenoid.Value.kForward
    set(value) = set(if (value) DoubleSolenoid.Value.kForward else DoubleSolenoid.Value.kReverse)
