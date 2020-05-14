package frc.excalibur.lib.trajectory

import edu.wpi.first.wpilibj.geometry.Pose2d
import edu.wpi.first.wpilibj.geometry.Transform2d
import edu.wpi.first.wpilibj.trajectory.Trajectory

/**
 * Transforms all poses in the trajectory by the given transform. This is
 * useful for converting a robot-relative trajectory into a field-relative
 * trajectory. This works with respect to the first pose in the trajectory.
 *
 * @param transform The transform to transform the trajectory by.
 * @return The transformed trajectory.
 */
infix fun Trajectory.transformBy(transform: Transform2d): Trajectory = this.transformBy(transform)

/**
 * Transforms all poses in the trajectory so that they are relative to the
 * given pose. This is useful for converting a field-relative trajectory
 * into a robot-relative trajectory.
 *
 * @param pose The pose that is the origin of the coordinate frame that
 * the current trajectory will be transformed into.
 * @return The transformed trajectory.
 */
infix fun Trajectory.relativeTo(pose: Pose2d): Trajectory = this.relativeTo(pose)
