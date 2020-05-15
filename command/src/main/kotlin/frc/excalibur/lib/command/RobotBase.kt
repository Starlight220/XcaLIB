package frc.excalibur.lib.command


internal class EventListener(private val event: Event, private val callback: () -> Unit) {
    operator fun invoke(old: Event.Type, new: Event.Type) {
        if (event.predicate(old, new)) callback()
    }
}

internal data class Event(val from: Type = Type.kAny, val to: Type = Type.kAny) {
    val predicate: (Type, Type) -> Boolean =
        { prev, new -> (from.qualifies(prev) && to.qualifies(new)) }

    enum class Type {
        kAny {
            override fun qualifies(other: Type): Boolean = true
        },
        kTeleop, kAuto, kDisabled, kPower;

        open fun qualifies(other: Type): Boolean {
            return when (other) {
                kAny -> return true
                else -> this == other
            }
        }

        infix fun to(other: Type) = Event(this, other)
    }

}