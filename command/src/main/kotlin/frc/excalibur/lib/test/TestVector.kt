package frc.excalibur.lib.test

import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.Notifier
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.CommandScheduler
import frc.excalibur.lib.command.XSubsystem
import frc.excalibur.lib.interfaces.Initiable
import frc.excalibur.lib.interfaces.Releasable

data class TestVector(private val vector: (Double) -> Unit, private val name: String) : Releasable {
    init {
        tests[name] = this
    }

    operator fun invoke(value: Double) = vector(value)
    override fun release() = vector(0.0)

    companion object TestThread : Initiable, Releasable {
        private val notifier: Notifier = Notifier(TestThread::run)

        private var tests: MutableMap<String, TestVector> = mutableMapOf()
        private var default = TestVector({ run { Unit } }, "none")

        private var chooser: SendableChooser<TestVector> = SendableChooser()
        private var entry = SmartDashboard.getEntry("testValue")

        private var lastSelected: TestVector = default
        private var selected: TestVector = chooser.selected


        override fun init() {
            //disable the scheduler, stop everything
            CommandScheduler.getInstance().disable()
            XSubsystem.subsystems.forEach {
                it.apply {
                    -this
                    initTestVectors()
                }
            }
            -this

            //initialize the SmartDashboard part
            entry.setNumber(0.0)
            chooser.setDefaultOption("none", default)
            tests.forEach(chooser::addOption)

            notifier.startPeriodic(0.2)
        }

        private fun run() {
            //if not enabled on test mode, exit
            if (DriverStation.getInstance().run { isDisabled || !isTest }) {
                -this
                return
            }

            selected = chooser.selected

            //if changed, zero the previous one
            if (selected != lastSelected) {
                lastSelected(0.0)
            }

            //apply value
            selected(entry.getDouble(0.0))

            //remember current selection for next iteration
            lastSelected = selected
        }

        override fun release() {
            notifier.stop()
            tests.forEach { (_, vector) -> -vector }
        }
    }
}

infix fun ((Double) -> Unit).withName(name: String) = TestVector(this, name)

interface Testable {
    fun initTestVectors()
}