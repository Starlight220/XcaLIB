package frc.excalibur.lib.devices.limelight

import edu.wpi.first.networktables.NetworkTableInstance

/**
 * Limelight [docs](https://docs.limelightvision.io/en/latest/)
 */
open class LimelightCamera(tablename: String = "limelight") {
    private val table = NetworkTableInstance.getDefault().getTable(tablename)
    private fun getEntry(data: String) = table.getEntry(data)

    /**
     * Cam Mode : 0 Vision, 1 Driving
     */
    var camMode: CamMode = CamMode.DRIVING
        set(mode) {
            field = mode
            getEntry("camMode").setNumber(mode.value)
        }
        get() = CamMode.fromNumber((getEntry("camMode").getNumber(1).toInt()))

    /**
     * LED mode : 0 (pipeline default), 1 (off), 2 (blink), 3 (on)
     */
    var ledMode: LedMode = LedMode.DEFAULT
        set(mode) {
            field = mode
            getEntry("ledMode").setNumber(mode.value)
        }
        get() = LedMode.fromNumber((getEntry("ledMode").getNumber(1).toInt()))

    /**
     * Pipeline number (between 0 and 10).
     */
    var pipeline: Int = 0
        set(value) {
            field = value
            getEntry("pipeline").setNumber(value)
        }
        get() = getEntry("pipeline").getNumber(0).toInt()

    /**
     * Horizontal Offset From Crosshair To Target
     * (LL1: -27 degrees to 27 degrees |
     * LL2: -29.8 to 29.8 degrees)
     */
    val tx: Double
        get() = getEntry("tx").getDouble(0.0)

    /**
     * Whether the limelight has any valid targets (0 or 1)
     */
    val tv: Boolean
        get() = getEntry("tv").getDouble(0.0).toInt() == 1

    /**
     * Vertical Offset From Crosshair To Target
     * (LL1: -20.5 degrees to 20.5 degrees | LL2: -24.85 to 24.85 degrees)
     */
    val ty: Double
        get() = getEntry("ty").getDouble(0.0)

    /**
     * Target Area (0% of image to 100% of image)
     */
    val ta: Double
        get() = getEntry("ta").getDouble(0.0)

    /**
     * Sidelength of shortest side of the fitted bounding box (pixels)
     */
    val tshort: Double
        get() = getEntry("tshort").getDouble(0.0)

    /**
     * Sidelength of longest side of the fitted bounding box (pixels)
     */
    val tlong: Double
        get() = getEntry("tlong").getDouble(0.0)

    /**
     * Horizontal sidelength of the rough bounding box (0 - 320 pixels)
     */
    val thor: Double
        get() = getEntry("thor").getDouble(0.0)

    /**
     * Vertical sidelength of the rough bounding box (0 - 320 pixels)
     */
    val tvert: Double
        get() = getEntry("tvert").getDouble(0.0)
    /*
        Values:
             Getters:
                 tv - Whether the limelight has any valid targets (0 or 1)
                 tx - Horizontal Offset From Crosshair To Target
                    (LL1: -27 degrees to 27 degrees | LL2: -29.8 to 29.8 degrees)
                 ty - Vertical Offset From Crosshair To Target
                 (LL1: -20.5 degrees to 20.5 degrees | LL2: -24.85 to 24.85 degrees)
                 ta - Target Area (0% of image to 100% of image)
                 tshort - Sidelength of shortest side of the fitted bounding box (pixels)
                 tlong - Sidelength of longest side of the fitted bounding box (pixels)
                 thor - Horizontal sidelength of the rough bounding box (0 - 320 pixels)
                 tvert - Vertical sidelength of the rough bounding box (0 - 320 pixels)
             Setters:
                 LED mode : 1 (off), 2 (blink), 3 (on)
                 Cam Mode : 0 Vision, 1 Driving
      */

    /**
     * Cam Mode : 0 Vision, 1 Driving
     */
    enum class CamMode(internal val value: Int) {
        /**
         * Processing pipeline is activated.
         */
        VISION(0),

        /**
         * Processing pipeline is off.
         */
        DRIVING(1);

        companion object {
            /**
             * Gets the [CamMode] constant that corresponds to [number]
             * @throws IllegalArgumentException if there is no corresponding constant.
             */
            fun fromNumber(number: Int): CamMode = when (number) {
                0 -> VISION
                1 -> DRIVING
                else -> throw IllegalArgumentException("No CamMode for value $number")
            }
        }
    }

    /**
     * LED mode : 0 (pipeline default), 1 (off), 2 (blink), 3 (on)
     */
    enum class LedMode(internal val value: Int) {
        /**
         * The pipeline default
         */
        DEFAULT(0),

        /**
         * Force off
         */
        OFF(1),

        /**
         * Force blink
         */
        BLINK(2),

        /**
         * Force on
         */
        ON(3);

        companion object {
            /**
             * Gets the [CamMode] constant that corresponds to [number]
             * @throws IllegalArgumentException if there is no corresponding constant.
             */
            fun fromNumber(number: Int): LedMode = when (number) {
                0 -> DEFAULT
                1 -> OFF
                2 -> BLINK
                3 -> ON
                else -> throw IllegalArgumentException("No CamMode for value $number")
            }
        }
    }

    /**
     * Add to this map to store vision pipelines by string values.
     */
    object Pipelines : Map<String, Int> by mutableMapOf()
}
