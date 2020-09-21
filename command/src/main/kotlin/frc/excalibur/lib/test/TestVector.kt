package frc.excalibur.lib.test

import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.Notifier
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.CommandScheduler
import frc.excalibur.lib.command.XSubsystem
import frc.excalibur.lib.interfaces.Initiable
import frc.excalibur.lib.interfaces.Releasable

/**
 * A convenient way to run a single "vector" of the robot (a motor, etc.) in test mode without much
 * boilerplate code. All that is needed is the following:
 * ```kotlin
 *     val motor: Spark = Spark(port)
 *     TestVector(motor::set, "motor")
 *```
 * and in the Robot class/object:
 * ```kotlin
 *      override fun testInit() {
 *          TestVector.TestThread()
 *      }
 * ```
 * A [SendableChooser] and number widgets will appear on the SmartDashboard.
 * Select the TestVector to run via the SendableChooser widget, and specify the input in the
 *
 *
 * @param vector the controlled parameter
 * @param name the name the [vector] should appear with on the SmartDashboard
 * @param release disables [vector]. Defaults to `vector(0.0)`.
 */
class TestVector(
    private val vector: (Double) -> Unit,
    private val name: String,
    release: () -> Unit = { vector(0.0) }
) : Releasable by Releasable.ReleaseLambda(release) {
    init {
        vectors[name] = this
    }

    private operator fun invoke(value: Double) = vector(value)

    /**
     * The main runner of the [TestVector] framework.
     * Use [TestThread()][invoke] to start running, will stop automatically when the robot exits
     * test mode or is disabled. If you need to stop running the TestThread from code, use
     * [-TestThread][release].
     */
    companion object TestThread : Initiable, Releasable {
        private val notifier: Notifier = Notifier(TestThread::run)

        private var vectors: MutableMap<String, TestVector> = mutableMapOf()
        private var default = TestVector({ run { Unit } }, "none")

        private var chooser: SendableChooser<TestVector> = SendableChooser()
        private var entry = SmartDashboard.getEntry("testValue")

        private var lastSelected: TestVector = default

        override fun init() {
            // disable the scheduler, stop everything
            CommandScheduler.getInstance().disable()
            XSubsystem.subsystems.forEach {
                it.apply {
                    -this
                    initTestVectors()
                }
            }
            -this

            // initialize the SmartDashboard part
            entry.setNumber(0.0)
            chooser.setDefaultOption("none", default)
            vectors.forEach(chooser::addOption)

            notifier.startPeriodic(0.2)
        }

        private fun run() {
            // if not enabled on test mode, exit
            if (DriverStation.getInstance().run { isDisabled || !isTest }) {
                -this
                return
            }
            var selected: TestVector = default
            chooser.selected?.let { selected = it }

            // if changed, zero the previous one
            if (selected != lastSelected) {
                -lastSelected
            }

            // apply value
            selected(entry.getDouble(0.0))

            // remember current selection for next iteration
            lastSelected = selected
        }

        override fun release() {
            notifier.stop()
            vectors.forEach { (_, vector) -> -vector }
        }
    }
}

/**
 * An infix function for creating [TestVector] objects quickly, with the default release.
 */
infix fun ((Double) -> Unit).withName(name: String): TestVector = TestVector(this, name)

/**
 * An interface that gives a convenient place to initialize all [TestVector]s of a certain class.
 */
interface Testable {
    /**
     * Initialize all [TestVector]s.
     */
    fun initTestVectors()
}
