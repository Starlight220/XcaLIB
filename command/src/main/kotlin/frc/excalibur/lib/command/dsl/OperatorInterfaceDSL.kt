package frc.excalibur.lib.command.dsl

import edu.wpi.first.wpilibj2.command.button.Trigger

fun (() -> Boolean).asTrigger(): Trigger = Trigger(this)

@OperatorInterfaceDSL
fun joystick(port: Int, builder: JoystickBuilder.() -> Unit) {
}

@OperatorInterfaceDSL
fun trigger(builder: TriggerBuilder.() -> Unit) {
    TriggerBuilder()
}

fun test() {
    joystick(1) {
        button(5)
    }
    trigger {
        predicate { true }
        whenActive {
        }
    }
}
