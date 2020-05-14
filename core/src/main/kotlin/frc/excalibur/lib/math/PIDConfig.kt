package frc.excalibur.lib.math

import edu.wpi.first.wpilibj.controller.PIDController

data class PIDConfig(
    val p : Double,
    val i : Double = 0.0,
    val d : Double = 0.0,
    val aff: Double = 0.0
){
    val wpiController: PIDController
        get() = PIDController(p, i, d)
}

