@file:Suppress("ClassName", "PropertyName")

package frc.excalibur.lib.ports

import edu.wpi.first.wpilibj.I2C
import edu.wpi.first.wpilibj.SPI


private interface PortDevice {
    val PWM: List<Int>
    val DIO: List<Int>
    val AnalogIn: List<Int>
    val SPI: SPI.Port
    val I2C: I2C.Port
}


object roboRIO : PortDevice {
    override val PWM: List<Int>
        get() = (0..9).toList()
    override val DIO: List<Int>
        get() = (0..9).toList()
    override val AnalogIn: List<Int>
        get() = (0..5).toList()
    val Relay: List<Int>
        get() = (0..5).toList()
    override val SPI: SPI.Port
        get() = edu.wpi.first.wpilibj.SPI.Port.kOnboardCS0
    override val I2C: I2C.Port
        get() = edu.wpi.first.wpilibj.I2C.Port.kOnboard
}


object CAN {
    private val list = (1..62).toList()

    operator fun get(index: Int): Int {
        return list[index]
    }
}

object PCM {
    operator fun get(index: Int): Int {
        return index
    }
}

/**
 * [link](https://pdocs.kauailabs.com/navx-mxp/installation/io-expansion/)
 */
object navX : PortDevice {
    override val PWM: List<Int>
        get() = (10..19).toList()
    override val DIO: List<Int>
        get() = ((10..13) + (18..23)).toList()
    override val AnalogIn: List<Int>
        get() = (4..7).toList()
    val AnalogOut: List<Int>
        get() = (0..1).toList()
    override val SPI: SPI.Port
        get() = edu.wpi.first.wpilibj.SPI.Port.kMXP
    override val I2C: I2C.Port
        get() = edu.wpi.first.wpilibj.I2C.Port.kMXP
}