package frc.excalibur.lib.trajectory

import edu.wpi.first.wpilibj.controller.RamseteController
import edu.wpi.first.wpilibj.geometry.Pose2d
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics
import edu.wpi.first.wpilibj.trajectory.Trajectory
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.RamseteCommand
import edu.wpi.first.wpilibj2.command.Subsystem
import java.util.function.BiConsumer
import java.util.function.Supplier

/**
 * A container class for decreasing the verbosity of constructing a [RamseteCommand].
 * @param poseSource the source of the robot pose. Can be the [odometry's `getPoseMeters()`]
 * [edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry.getPoseMeters] method.
 * @param controller the [RamseteController]. For the default Ramsete constants,
 * don't use this parameter.
 * @param kinematics the [DifferentialDriveKinematics] object of the drivetrain. Use the other
 * constructor if you prefer to specify the trackwidth.
 * @param speedConsumer the consumer of the **raw velocity setpoints** returned from the
 * [RamseteController], without feedforward/PID processing. Suggested use is giving these
 * setpoints to the drivetrain motor controllers for onboard velocity PID control.
 * @param drivetrain the drivetrain [Subsystem].
 * @param neutralDrive a lambda to stop the drivetrain. Defaults to `speedConsumer(0.0, 0.0)`.
 */
data class RamseteConfig(
    val poseSource: () -> Pose2d,
    val controller: RamseteController = RamseteController(),
    val kinematics: DifferentialDriveKinematics,
    val speedConsumer: (Double, Double) -> Unit,
    val drivetrain: Subsystem,
    val neutralDrive: () -> Unit = { speedConsumer(0.0, 0.0) }
) {
    /**
     * A container class for decreasing the verbosity of constructing a [RamseteCommand].
     * @param poseSource the source of the robot pose. Can be the [odometry's `getPoseMeters()`]
     * [edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry.getPoseMeters] method.
     * @param controller the [RamseteController]. For the default Ramsete constants, don't use this
     * parameter.
     * @param trackWidth the trackwidth of the drivetrain. Use the other constructor if you prefer
     * to use a [DifferentialDriveKinematics] object.
     * @param speedConsumer the consumer of the **raw velocity setpoints** returned from the
     * [RamseteController], without feedforward/PID processing. Suggested use is giving these
     * setpoints to the drivetrain motor controllers for onboard velocity PID control.
     * @param drivetrain the drivetrain [Subsystem].
     * @param neutralDrive a lambda to stop the drivetrain. Defaults to `speedConsumer(0.0, 0.0)`.
     */
    constructor(
        poseSource: () -> Pose2d,
        controller: RamseteController = RamseteController(),
        trackWidth: Double,
        speedConsumer: (Double, Double) -> Unit,
        drivetrain: Subsystem,
        neutralDrive: () -> Unit = { speedConsumer(0.0, 0.0) }
    ) : this(
        poseSource,
        controller,
        DifferentialDriveKinematics(trackWidth),
        speedConsumer,
        drivetrain,
        neutralDrive
    )

    /**
     * Builds a [RamseteCommand] to follow [trajectory] with a stop command appended to it.
     * @param trajectory the [Trajectory] to follow.
     */
    fun getRamsete(trajectory: Trajectory): Command = RamseteCommand(
        trajectory,
        Supplier(poseSource),
        controller,
        kinematics,
        BiConsumer(speedConsumer),
        drivetrain
    ).andThen(Runnable(neutralDrive), drivetrain)
}
