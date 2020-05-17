package frc.excalibur.lib.math

import edu.wpi.first.wpilibj.controller.PIDController

/**
 * A class for storing "configs" for PID controllers.
 */
data class PIDConfig(
    /**
     * The kP constant of the controller.
     */
    val p: Double,
    /**
     * The kI constant of the controller.
     */
    val i: Double = 0.0,
    /**
     * The kD constant of the controller.
     */
    val d: Double = 0.0,
    /**
     * The arbitrary-feedforward constant of the controller.
     */
    val aff: Double = 0.0
) {
    /**
     * Create a
     */
    val wpiController: PIDController
        get() = PIDController(p, i, d)
}

