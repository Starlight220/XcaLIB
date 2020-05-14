package frc.excalibur.lib.trajectory

import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.geometry.Pose2d
import edu.wpi.first.wpilibj.trajectory.*
import frc.excalibur.lib.util.Cache
import frc.excalibur.lib.util.lang.asDeployPath
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap

private typealias TrajectoryMap = HashMap<String, TrajectorySource>

private operator fun TrajectoryMap.get(name: String, default: TrajectorySource): TrajectorySource =
        getOrDefault(name, default)

sealed class TrajectorySource {
    companion object Trajectories {
        private val cache: Cache =
            Cache()
        private val map: TrajectoryMap = TrajectoryMap()

        operator fun get(name: String): Trajectory = map[name, NullTrajectory].getTrajectory()

        fun loadAll() {
            map.values.forEach {it.getTrajectory()}
        }

        fun log(src: TrajectorySource) {
            map[src.name] = src
        }

        inline fun logAll(block: Trajectories.() -> Unit) {
            block(this)
            loadAll()
        }

        fun log(vararg sources: TrajectorySource) {
            sources.forEach(Trajectories::log)
        }

        fun log(src: () -> Trajectory, name: String) = log(LambdaTrajectory(src, name))
        fun log(name: String, file: String) = log(JsonTrajectory(name, file))
        fun log(src: List<Pose2d>, name: String) = log(OnboardTrajectory(src, name))
    }

    protected abstract val name: String

    abstract fun getTrajectory(): Trajectory

    object NullTrajectory : TrajectorySource() {
        override val name: String = "null"
        override fun getTrajectory(): Trajectory = Trajectory(Collections.emptyList())
    }

    class JsonTrajectory(
            override val name: String,
            private val file: String = name
    ) : TrajectorySource() {
        override fun getTrajectory(): Trajectory = cache(name) {
            try {
                val path = "output/$file.wpilib.json".asDeployPath()
                return@cache TrajectoryUtil.fromPathweaverJson(path)
            } catch (e: IOException) {
                DriverStation.reportError("file trajectory $name generation failed : ${e.message}", true)
                return@cache NullTrajectory.getTrajectory()
            }
        }
    }

    class OnboardTrajectory(
            private val poses: List<Pose2d>,
            override val name: String = poses.toString()
    ) : TrajectorySource() {
        companion object {
            private val config = TrajectoryConfig(10.0, 10.3)
        }

        override fun getTrajectory(): Trajectory = cache(poses.toString()) {
            TrajectoryGenerator.generateTrajectory(poses, config)
        }
    }

    class LambdaTrajectory(
            private val supplier: () -> Trajectory,
            override val name: String
    ) : TrajectorySource() {
        override fun getTrajectory(): Trajectory = supplier.invoke()
    }
}
