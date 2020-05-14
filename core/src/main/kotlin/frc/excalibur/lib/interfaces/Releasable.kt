package frc.excalibur.lib.interfaces

interface Releasable {
    fun release()
    operator fun unaryMinus() = release()
}