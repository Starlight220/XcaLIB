package frc.excalibur.lib.util.pipeline

class Pipeline<START, END>(
    private val stages: List<PipelineStage<*, *>> = listOf()
) : PipelineStage<START, END> {

    override fun apply(value: START): END {
        TODO()
    }
}

interface PipelineStage<IN, OUT> {
    fun apply(value: IN): OUT

    operator fun invoke(value: IN): OUT = apply(value)
}
