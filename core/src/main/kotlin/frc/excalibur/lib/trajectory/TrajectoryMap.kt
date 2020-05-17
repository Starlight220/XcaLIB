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

/**
 * A class representing a source for a [Trajectory], with a subclass for each type -
 * [lambdas][LambdaTrajectory], [PathWeaver-style JSON files][JsonTrajectory], and
 * [trajectories created onboard][OnboardTrajectory].
 */
sealed class TrajectorySource {
    /**
     * Use this object to store the [Trajectories][Trajectory] used. Has a [caching mechanism]
     * [Cache] to prevent calculating the same [Trajectory] twice.
     */
    companion object Trajectories {
        private val cache: Cache = Cache()
        private val map: TrajectoryMap = TrajectoryMap()

        /**
         * Gets a [Trajectory] stored. If there is no stored trajectory for the given [name],
         * [NullTrajectory] will be returned.
         */
        operator fun get(name: String): Trajectory = map[name, NullTrajectory].getTrajectory()

        /**
         * Caches all the trajectories.
         */
        fun loadAll() {
            map.values.forEach { it.getTrajectory() }
        }

        /**
         * Adds a [TrajectorySource] to the registry.
         */
        fun log(src: TrajectorySource) {
            map[src.name] = src
        }

        /**
         * Gives a convenient block format to log all trajectories.
         * After all trajectories are logged ([block] is run), all trajectories are loaded.
         */
        inline fun logAll(block: Trajectories.() -> Unit) {
            block(this)
            loadAll()
        }

        /**
         * Logs some amount of [TrajectorySource] objects.
         */
        fun log(vararg sources: TrajectorySource) {
            sources.forEach(Trajectories::log)
        }

        /**
         * Logs a lambda as a [LambdaTrajectory].
         */
        fun log(src: () -> Trajectory, name: String): Unit = log(LambdaTrajectory(src, name))

        /**
         * Logs a file as a [JsonTrajectory].
         */
        fun log(name: String, file: String): Unit = log(JsonTrajectory(name, file))

        /**
         * Logs a list of [Pose2d] objects as a [OnboardTrajectory].
         */
        fun log(src: List<Pose2d>, name: String): Unit = log(OnboardTrajectory(src, name))
    }

    internal abstract val name: String

    internal abstract fun getTrajectory(): Trajectory

    /**
     * An empty trajectory.
     */
    object NullTrajectory : TrajectorySource() {
        override val name: String = "null"
        override fun getTrajectory(): Trajectory = Trajectory(Collections.emptyList())
    }

    /**
     * A trajectory generated from a PathWeaver-style JSON file.
     */
    class JsonTrajectory(
        override val name: String,
        private val file: String = name
    ) : TrajectorySource() {
        override fun getTrajectory(): Trajectory = cache(name) {
            try {
                val path = "output/$file.wpilib.json".asDeployPath()
                return@cache TrajectoryUtil.fromPathweaverJson(path)
            } catch (e: IOException) {
                DriverStation.reportError(
                    "file trajectory $name generation failed : ${e.message}",
                    true
                )
                return@cache NullTrajectory.getTrajectory()
            }
        }
    }


    /**
     * A trajectory computed onboard the RIO.
     */
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

    /**
     * A trajectory from a lambda expression.
     * Theoretically this can be used instead of any of the other types, but for performance and
     * code organization users should try to use the most specific [TrajectorySource]
     * implementation.
     */
    class LambdaTrajectory(
        private val supplier: () -> Trajectory,
        override val name: String
    ) : TrajectorySource() {
        override fun getTrajectory(): Trajectory = supplier.invoke()
    }
}
