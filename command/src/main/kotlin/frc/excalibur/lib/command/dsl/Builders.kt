package frc.excalibur.lib.command.dsl

import edu.wpi.first.wpilibj.Joystick
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Subsystem
import edu.wpi.first.wpilibj2.command.button.JoystickButton
import edu.wpi.first.wpilibj2.command.button.Trigger

@DslMarker
annotation class OperatorInterfaceDSL

private fun createButtonList(joystick: Joystick): List<() -> Boolean> {
    val list = mutableListOf<() -> Boolean>()
    for (idx in 1..joystick.buttonCount) {
        list.add(idx) { joystick.getRawButton(idx) }
    }
    return list
}

private fun createAxisList(joystick: Joystick): List<() -> Double> {
    val list = mutableListOf<() -> Double>()
    for (idx in 1..joystick.axisCount) {
        list.add(idx) { joystick.getRawAxis(idx) }
    }
    return list
}

@OperatorInterfaceDSL
class JoystickBuilder private constructor(port: Int) {
    private val joystick = Joystick(port)

    val buttons: List<() -> Boolean> = createButtonList(joystick)

    val axes: List<() -> Double> = createAxisList(joystick)

    fun button(port: Int, builder: ButtonBuilder.() -> Unit) {
        builder(ButtonBuilder(this, port))
    }

    fun button(port: Int): JoystickButton {
        return createJoystickButton(port)
    }

    private val buttonMap: MutableMap<Int, JoystickButton> = mutableMapOf()
    internal fun createJoystickButton(port: Int): JoystickButton = buttonMap.getOrPut(port) {
        JoystickButton(
            this.joystick,
            port
        )
    }

    companion object {
        private val joystickMap: MutableMap<Int, JoystickBuilder> = mutableMapOf()
        internal operator fun invoke(port: Int): JoystickBuilder = joystickMap.getOrPut(port) {
            JoystickBuilder(port)
        }
    }
}

class ButtonBuilder internal constructor(joystick: JoystickBuilder, port: Int) {
    private val button: JoystickButton = joystick.createJoystickButton(port)

    fun whenPressed(command: Command) {
        button.whenPressed(command)
    }

    fun whenPressed(vararg requirements: Subsystem, runnable: () -> Unit) {
        button.whenPressed(runnable, *requirements)
    }

    fun whileHeld(command: Command) {
        button.whileHeld(command)
    }

    fun whileHeld(vararg requirements: Subsystem, runnable: () -> Unit) {
        button.whileHeld(runnable, *requirements)
    }

    fun whenReleased(command: Command) {
        button.whenReleased(command)
    }

    fun whenReleased(vararg requirements: Subsystem, runnable: () -> Unit) {
        button.whenReleased(runnable, *requirements)
    }

    fun toggleWhenPressed(command: Command) {
        button.toggleWhenPressed(command)
    }

    fun cancelWhenPressed(command: Command) {
        button.cancelWhenPressed(command)
    }
}

class TriggerBuilder internal constructor() {
    private lateinit var trigger: Trigger

    fun predicate(predicate: () -> Boolean) {
        trigger = Trigger(predicate)
    }

    fun whenActive(command: Command) {
        trigger.whenActive(command)
    }

    fun whenActive(vararg requirements: Subsystem, runnable: () -> Unit) {
        trigger.whenActive(runnable, *requirements)
    }

    fun whileActiveContinuous(command: Command) {
        trigger.whileActiveContinuous(command)
    }

    fun whileActiveContinuous(vararg requirements: Subsystem, runnable: () -> Unit) {
        trigger.whileActiveContinuous(runnable, *requirements)
    }

    fun whenInactive(command: Command) {
        trigger.whenInactive(command)
    }

    fun whenInactive(vararg requirements: Subsystem, runnable: () -> Unit) {
        trigger.whenInactive(runnable, *requirements)
    }

    fun toggleWhenActive(command: Command) {
        trigger.toggleWhenActive(command)
    }

    fun cancelWhenActive(command: Command) {
        trigger.cancelWhenActive(command)
    }
}
