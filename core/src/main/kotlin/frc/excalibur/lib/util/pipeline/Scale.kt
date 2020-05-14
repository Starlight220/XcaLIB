package frc.excalibur.lib.util.pipeline

import frc.excalibur.lib.util.pipeline.PipelineStage

class ScaleDouble(private val maxIn : Double,
                   private val maxOut : Double,
                   private val minIn : Double,
                   private val minOut : Double
) : PipelineStage<Double, Double> {

    override fun apply(value: Double): Double {
        return (value - minIn) / (minOut - minIn) * (maxOut - maxIn) + maxIn
    }
}

class ScaleInt(private val maxIn : Int,
                  private val maxOut : Int,
                  private val minIn : Int,
                  private val minOut : Int
) : PipelineStage<Int, Int> {

    override fun apply(value: Int): Int {
        return (value - minIn) / (minOut - minIn) * (maxOut - maxIn) + maxIn
    }
}
