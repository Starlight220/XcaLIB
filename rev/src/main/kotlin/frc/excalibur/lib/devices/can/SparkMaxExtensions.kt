package frc.excalibur.lib.devices.can

import com.revrobotics.CANPIDController
import com.revrobotics.CANSparkMax.ExternalFollower.kFollowerDisabled
import com.revrobotics.ControlType
import frc.excalibur.lib.math.PIDConfig
import com.revrobotics.CANError as Error
import com.revrobotics.CANSparkMax as SparkMax
import edu.wpi.first.wpilibj.DriverStation as DS
import com.revrobotics.CANEncoder as Encoder

typealias DoublePair = Pair<Double, Double>

/**
 * @param percent the throttle, between 1.0 and -1.0
 */
fun CANPIDController.setAbsolutePercent(percent : Double) =
        !setReference(percent, ControlType.kDutyCycle)

/**
 * Inverts this controller.
 */
operator fun SparkMax.not() : SparkMax{
    this.inverted = true
    return this
}

/**
 * Inverts this encoder.
 */
operator fun Encoder.not() : Encoder{
    !this.setInverted(true)
    return this
}

/**
* Applies the values of a PIDConfig object to this PID controller.
* @param config the config object
* @receiver the PID controller that is configured
*/
infix fun CANPIDController.configuredBy(config : PIDConfig) : CANPIDController =
       config.applyREVController(this)

/**
 * Reports an error to the DS if this object isn't [CANError.kOk][Error.kOk]
 */
operator fun Error.not(){
    if(this != Error.kOk){
        DS.reportError("SparkMax config failed : $this", false)
    }
}

/**
 * Follows another SparkMax, in the same direction.
 * @receiver the follower/slave controller
 * @param master the leader/master controller
 */
infix fun SparkMax.follows(master: SparkMax) : SparkMax{
    !this.follow(master)
    return this
}

/**
 * Follows another SparkMax, in the opposite direction.
 * @receiver the follower/slave controller
 * @param master the leader/master controller
 */
infix fun SparkMax.opposes(master: SparkMax) : SparkMax{
    !this.follow(master, true)
    return this
}
//
//infix fun SparkMax.follows(master: TalonSRX){
//    this.follow(SparkMax.ExternalFollower.kFollowerPhoenix, master.deviceID)
//}

/**
 * Sets the position and velocity factors.
 */
operator fun Encoder.times(factors : DoublePair) : Encoder{
    val (posFactor, velFactor) = factors
    !this.setPositionConversionFactor(posFactor)
    !this.setVelocityConversionFactor(velFactor)
    return this
}

/**
 * Resets the encoders, restores config parameters to factory defaults, and disables following.
 */
operator fun SparkMax.unaryMinus(): SparkMax{
    -this.encoder
    -this.alternateEncoder
    !this.restoreFactoryDefaults(false)
    !this.follow(kFollowerDisabled, 0)
    return this
}

/**
 * Resets the encoder position.
 */
operator fun Encoder.unaryMinus(){
    !this.setPosition(0.0)
}


fun PIDConfig.applyREVController(controller : CANPIDController): CANPIDController{
   !controller.setP(p)
   !controller.setI(i)
   !controller.setD(d)
   !controller.setFF(aff)
   return controller
}