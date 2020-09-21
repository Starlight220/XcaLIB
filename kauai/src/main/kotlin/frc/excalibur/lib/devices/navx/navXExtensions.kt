package frc.excalibur.lib.devices.navx

import com.kauailabs.navx.frc.AHRS
import edu.wpi.first.wpilibj.geometry.Rotation2d

/**
 * Resets the gyro angle.
 */
operator fun AHRS.unaryMinus(): Unit = this.reset()

/**
 * Gets a [Rotation2d] object based on [AHRS.getAngle]
 */
fun AHRS.getRotation(): Rotation2d = Rotation2d.fromDegrees(angle)
