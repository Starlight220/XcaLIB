package frc.excalibur.lib.command

import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.excalibur.lib.interfaces.Initiable
import frc.excalibur.lib.interfaces.Releasable
import frc.excalibur.lib.test.Testable

abstract class XSubsystem : SubsystemBase(), Releasable, Testable, Initiable {
    companion object {
        /**
         * All instances of XSubsystem.
         * The Robot class should have an identical property that is delegated to this one.
         */
        val subsystems: MutableSet<XSubsystem> = mutableSetOf()
    }

    override fun initTestVectors() {

    }

    override fun init() {

    }
}