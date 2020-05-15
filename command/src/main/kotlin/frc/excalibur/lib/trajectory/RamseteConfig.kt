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

data class RamseteConfig(
    val poseSource: () -> Pose2d,
    val controller: RamseteController = RamseteController(),
    val kinematics: DifferentialDriveKinematics,
    val speedConsumer: (Double, Double) -> Unit,
    val drivetrain: Subsystem,
    val neutralDrive: () -> Unit = { speedConsumer(0.0, 0.0) }
) {
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

    fun getRamsete(trajectory: Trajectory): Command = RamseteCommand(
        trajectory,
        Supplier(poseSource),
        controller,
        kinematics,
        BiConsumer(speedConsumer),
        drivetrain
    ).andThen(Runnable { neutralDrive() }, drivetrain)
}