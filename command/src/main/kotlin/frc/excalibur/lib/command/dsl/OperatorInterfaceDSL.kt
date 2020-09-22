package frc.excalibur.lib.command.dsl

import edu.wpi.first.wpilibj2.command.FunctionalCommand

/**
 * !!!THIS IS EXAMPLE AND EXPERIMENTATION CODE ONLY!!!
 */
private fun test() {
    joystick(1) {
        button(3) {
            whenReleased(FunctionalCommand(null, null, null, buttons[8]))
        }
        button(4).whileHeld {
        }
    }
    trigger {
        predicate { true }
        whenActive {
            println("hi there")
        }
    }
}
