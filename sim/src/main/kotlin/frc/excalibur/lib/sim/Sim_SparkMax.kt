package sim

import com.revrobotics.AlternateEncoderType
import edu.wpi.first.wpilibj.PWM
import frc.excalibur.lib.sim.Sim_Encoder
import frc.excalibur.lib.sim.Sim_PIDController
import frc.excalibur.lib.sim.Sim_Wrapper
import kotlin.Boolean
import kotlin.Double
import kotlin.Int
import kotlin.TODO
import com.revrobotics.CANEncoder as Encoder
import com.revrobotics.CANError as Error
import com.revrobotics.CANPIDController as PIDController
import com.revrobotics.CANSparkMax as SparkMax

class Sim_SparkMax(
    id: Int,
    posOrVel: Boolean
) : SparkMax(id, MotorType.kBrushless), Sim_Wrapper {
    private var _isInverted: Boolean = false
    private var _encoder: Encoder = Sim_Encoder(this, 10.0)
    private var _altencoder: Encoder = Sim_Encoder(this, 10.0)
    private var _followers = mutableListOf<Sim_SparkMax>()
    private var _pidController = Sim_PIDController(this, posOrVel)
    private var _assignedSpeed = 0.0
    private var _pwm = PWM(id)

    init {
        Sim_Wrapper.init(this)
    }

    override fun getEncoder(): Encoder {
        return _encoder
    }

    override fun setEncPosition(value: Double): Error {
        _encoder.position = value
        return Error.kOk
    }


    override fun burnFlash(): Error {
        return Error.kOk
    }

    override fun follow(leader: SparkMax?): Error {
        leader?.apply { if (leader is Sim_SparkMax) leader._followers.add(this@Sim_SparkMax) }
        return Error.kOk
    }

    override fun follow(leader: SparkMax, invert: Boolean): Error {
        _isInverted = invert
        return follow(leader)
    }

    override fun follow(leader: ExternalFollower?, deviceID: Int): Error {
        TODO()
    }

    override fun follow(leader: ExternalFollower?, deviceID: Int, invert: Boolean): Error {
        TODO()
    }

    override fun getPIDController(): PIDController {
        return _pidController
    }

    override fun setIdleMode(mode: IdleMode?): Error {

        return Error.kOk
    }


    override fun getAlternateEncoder(): Encoder {
        return _altencoder
    }

    override fun getAlternateEncoder(sensorType: AlternateEncoderType?, counts_per_rev: Int): Encoder {
        return _altencoder
    }

    override fun getInverted(): Boolean {
        return _isInverted
    }


    override fun setAltEncPosition(value: Double): Error {
        _altencoder.position = value
        return Error.kOk
    }

    override fun get(): Double {
        return _assignedSpeed
    }

    override fun restoreFactoryDefaults(): Error {
        return Error.kOk
    }

    override fun setInverted(isInverted: Boolean) {
        _isInverted = isInverted
    }

    override fun set(speed: Double) {
        _assignedSpeed = speed
        _pwm.speed = speed
    }

    override fun setIAccum(value: Double): Error {
        _pidController.iAccum = value
        return Error.kOk
    }

    override fun periodic() {
        setEncPosition(
            _encoder.position +
                    (_assignedSpeed * (if (_encoder is Sim_Encoder) (_encoder as Sim_Encoder).rate else 1.0))
        )
        setAltEncPosition(
            _altencoder.position +
                    (_assignedSpeed * (if (_altencoder is Sim_Encoder) (_altencoder as Sim_Encoder).rate else 1.0))
        )

    }
}