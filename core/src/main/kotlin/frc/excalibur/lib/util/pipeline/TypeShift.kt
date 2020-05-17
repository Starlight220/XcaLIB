package frc.excalibur.lib.util.pipeline

abstract class TypeShift<T1, T2> : PipelineStage<T1, T2>

class NumberShift<T1 : Number, T2 : Number> : TypeShift<T1, T2>() {
    override fun apply(value: T1): T2 {
        TODO("Not yet implemented")
    }
}