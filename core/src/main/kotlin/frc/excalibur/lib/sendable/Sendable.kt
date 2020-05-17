package frc.excalibur.lib.sendable

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard

/**
 * Creates a [SendableChooser] with the given entries, and adds it to the [SmartDashboard]
 */
fun <V> sendableChooser(vararg values: Pair<String, V>): SendableChooser<V> = SendableChooser<V>()
    .apply {
        values.forEach { (k: String, v: V) -> this.addOption(k, v) }
        SmartDashboard.putData(this)
    }