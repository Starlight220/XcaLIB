package frc.excalibur.lib.interfaces

interface Initiable {
    fun init()
    operator fun invoke() = this.init()
}
