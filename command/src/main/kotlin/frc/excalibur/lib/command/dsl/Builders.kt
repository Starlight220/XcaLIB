package frc.excalibur.lib.command.dsl

import edu.wpi.first.wpilibj.Joystick
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Subsystem
import edu.wpi.first.wpilibj2.command.button.JoystickButton
import edu.wpi.first.wpilibj2.command.button.Trigger

@DslMarker
internal annotation class OperatorInterfaceDSL

private fun createButtonList(joystick: Joystick): List<Source<Boolean>> {
    val list = mutableListOf<Source<Boolean>>()
    for (idx in 1..joystick.buttonCount) {
        list.add(idx) { joystick.getRawButton(idx) }
    }
    return list
}

private fun createAxisList(joystick: Joystick): List<Source<Double>> {
    val list = mutableListOf<Source<Double>>()
    for (idx in 1..joystick.axisCount) {
        list.add(idx) { joystick.getRawAxis(idx) }
    }
    return list
}

/**
 * A type-safe builder for joysticks
 */
@OperatorInterfaceDSL
fun joystick(port: Int, builder: JoystickBuilder.() -> Unit): Unit = JoystickBuilder(port).builder()

/**
 * A receiver builder for joysticks
 */
@OperatorInterfaceDSL
class JoystickBuilder private constructor(port: Int) {
    private val joystick = Joystick(port)

    /**
     * Part of a shorthand syntax to pass a button as a `BooleanSupplier` / `() -> Boolean` value.
     *
     * For example:
     * ```kotlin
     * buttons[2]
     * ```
     * returns the button on port 2 on the joystick as a boolean source.
     */
    val buttons: List<Source<Boolean>> = createButtonList(joystick)

    /**
     * Part of a shorthand syntax to pass an axis as a `DoubleSupplier` / `() -> Double`
     * value.
     *
     * For example:
     * ```kotlin
     * axes[2]
     * ```
     * returns the axis on port 2 on the joystick as a double source.
     */
    val axes: List<Source<Double>> = createAxisList(joystick)

    /**
     * Creates a receiver builder for a specific button on this joystick.
     * If only a single binding is wanted, use of the [`button(port: Int)`][button] overload
     * provides for shorter and less nested code.
     *
     * @param port the button number/port on the joystick
     */
    fun button(port: Int, builder: ButtonBuilder.() -> Unit): Unit = button(port).builder()

    /**
     * Creates a receiver builder for a specific button on this joystick.
     * If more than a single binding is wanted, use of the [`button(port: Int, builder:
     * ButtonBuilder.() -> Unit)`][button] overload provides a more nested/builder-like syntax.
     *
     * @param port the button number/port on the joystick
     */
    fun button(port: Int): ButtonBuilder = ButtonBuilder(this, port)

    // internal use
    private val buttonMap: MutableMap<Int, JoystickButton> = mutableMapOf()
    internal fun createJoystickButton(port: Int): JoystickButton = buttonMap.getOrPut(port) {
        JoystickButton(joystick, port)
    }

    companion object {
        private val joystickMap: MutableMap<Int, JoystickBuilder> = mutableMapOf()
        internal operator fun invoke(port: Int): JoystickBuilder = joystickMap.getOrPut(port) {
            JoystickBuilder(port)
        }
    }
}

/**
 * A receiver builder for a specific button on a port.
 * Create using [JoystickBuilder.button(Int)][JoystickBuilder.button]
 */
@OperatorInterfaceDSL
class ButtonBuilder internal constructor(private val joystick: JoystickBuilder, port: Int) {
    private val button: JoystickButton = joystick.createJoystickButton(port)

    /**
     * @see JoystickBuilder.buttons
     */
    val buttons: List<Source<Boolean>>
        get() = joystick.buttons

    /**
     * @see JoystickBuilder.axes
     */
    val axes: List<Source<Double>>
        get() = joystick.axes

    /**
     * @see JoystickButton.whenPressed
     */
    fun whenPressed(command: Command) {
        button.whenPressed(command)
    }

    /**
     * @see JoystickButton.whenPressed
     */
    fun whenPressed(vararg requirements: Subsystem, runnable: () -> Unit) {
        button.whenPressed(runnable, *requirements)
    }

    /**
     * @see JoystickButton.whileHeld
     */
    fun whileHeld(command: Command) {
        button.whileHeld(command)
    }

    /**
     * @see JoystickButton.whileHeld
     */
    fun whileHeld(vararg requirements: Subsystem, runnable: () -> Unit) {
        button.whileHeld(runnable, *requirements)
    }

    /**
     * @see JoystickButton.whenReleased
     */
    fun whenReleased(command: Command) {
        button.whenReleased(command)
    }

    /**
     * @see JoystickButton.whenReleased
     */
    fun whenReleased(vararg requirements: Subsystem, runnable: () -> Unit) {
        button.whenReleased(runnable, *requirements)
    }

    /**
     * @see JoystickButton.toggleWhenPressed
     */
    fun toggleWhenPressed(command: Command) {
        button.toggleWhenPressed(command)
    }

    /**
     * @see JoystickButton.cancelWhenPressed
     */
    fun cancelWhenPressed(command: Command) {
        button.cancelWhenPressed(command)
    }
}

/**
 * Starts a type-safe builder for use of the Trigger API.
 *
 * **!!!Make sure that the first function you call in the builder block is
 * [TriggerBuilder.predicate], otherwise [UninitializedPropertyAccessException] will be thrown!!!**
 *
 * @throws UninitializedPropertyAccessException if [TriggerBuilder.predicate] isn't the first
 * function called in the builder block
 */
@OperatorInterfaceDSL
@Throws(UninitializedPropertyAccessException::class)
fun trigger(builder: TriggerBuilder.() -> Unit): Unit = TriggerBuilder().builder()

/**
 * A receiver builder for a specific button on a port.
 * Create using [trigger()][trigger]
 */
@OperatorInterfaceDSL
class TriggerBuilder internal constructor() {
    private lateinit var trigger: Trigger

    /**
     * Initialize the predicate on which this trigger is based.
     *
     * **This must be the first function called in the block!**
     */
    fun predicate(predicate: Source<Boolean>) {
        trigger = predicate.asTrigger()
    }

    /**
     * @see [Trigger.whenActive]
     */
    fun whenActive(command: Command) {
        trigger.whenActive(command)
    }

    /**
     * @see [Trigger.whenActive]
     */
    fun whenActive(vararg requirements: Subsystem, runnable: () -> Unit) {
        trigger.whenActive(runnable, *requirements)
    }

    /**
     * @see [Trigger.whileActiveContinuous]
     */
    fun whileActiveContinuous(command: Command) {
        trigger.whileActiveContinuous(command)
    }

    /**
     * @see [Trigger.whileActiveContinuous]
     */
    fun whileActiveContinuous(vararg requirements: Subsystem, runnable: () -> Unit) {
        trigger.whileActiveContinuous(runnable, *requirements)
    }

    /**
     * @see [Trigger.whenInactive]
     */
    fun whenInactive(command: Command) {
        trigger.whenInactive(command)
    }

    /**
     * @see [Trigger.whenInactive]
     */
    fun whenInactive(vararg requirements: Subsystem, runnable: () -> Unit) {
        trigger.whenInactive(runnable, *requirements)
    }

    /**
     * @see [Trigger.toggleWhenActive]
     */
    fun toggleWhenActive(command: Command) {
        trigger.toggleWhenActive(command)
    }

    /**
     * @see [Trigger.cancelWhenActive]
     */
    fun cancelWhenActive(command: Command) {
        trigger.cancelWhenActive(command)
    }
}

/**
 *
 */
typealias Source<T> = () -> T

/**
 * Get a [Trigger] object based on this `BooleanSupplier`.
 * @sample TriggerBuilder.predicate
 */
fun Source<Boolean>.asTrigger(): Trigger = Trigger(this)
