package frc.excalibur.lib.sim

import com.revrobotics.CANEncoder
import com.revrobotics.CANSensor
import com.revrobotics.CANSparkMax
import com.revrobotics.ControlType
import edu.wpi.first.wpilibj.controller.PIDController
import kotlin.Boolean
import kotlin.Double
import kotlin.TODO
import com.revrobotics.CANError as Error
import com.revrobotics.CANPIDController as PidController

internal class Sim_PIDController(
    private val device: CANSparkMax,
    private val controlType: Boolean
) :
    PidController(device),
    Sim_Wrapper {
    private var _i: Double = 0.0
    private var _d: Double = 0.0
    private var _iZone: Double = 0.0
    private var _iMaxAccum: Double = 0.0
    private var _feedbackSensor: CANEncoder = device.encoder
    private var _iAccum: Double = 0.0
    private var _maxOut: Double = 0.0
    private var _minOut: Double = 0.0
    private var _p: Double = 0.0
    private var _ff: Double = 0.0
    private var _dFilter: Double = 0.0
    private var _reference: Double = 0.0
    private var _controlType: ControlType = ControlType.kDutyCycle
    private var _mockController: PIDController = PIDController(_p, _i, _d)

    init {
        Sim_Wrapper.init(this)
    }

    override fun setFeedbackDevice(sensor: CANSensor?): Error {
        if (sensor != null) {
            _feedbackSensor = sensor as CANEncoder
        }
        return Error.kOk
    }

    override fun getD(): Double {
        return _d
    }

    override fun setIZone(IZone: Double): Error {
        _iZone = IZone
        return Error.kOk
    }

    override fun getIZone(): Double {
        return _iZone
    }

    override fun getIAccum(): Double {
        return _iAccum
    }

    override fun getOutputMin(): Double {
        return _minOut
    }

    override fun setP(gain: Double): Error {
        _p = gain
        return Error.kOk
    }

    override fun setDFilter(gain: Double): Error {
        _dFilter = gain
        return Error.kOk
    }

    override fun setD(gain: Double): Error {
        _d = gain
        return Error.kOk
    }


    override fun setFF(gain: Double): Error {
        _ff = gain
        return Error.kOk
    }


    override fun getP(): Double {
        return _p
    }


    override fun getFF(): Double {
        return _ff
    }


    override fun setReference(value: Double, ctrl: ControlType?): Error {
        _reference = value
        if (ctrl != null) {
            _controlType = ctrl
        }
        return Error.kOk
    }


    override fun setOutputRange(min: Double, max: Double): Error {
        _minOut = min
        _maxOut = max
        return Error.kOk
    }


    override fun getI(): Double {
        return _i
    }

    override fun setIAccum(iAccum: Double): Error {
        _iAccum = iAccum
        return Error.kOk
    }

    override fun getOutputMax(): Double {
        return _maxOut
    }

    override fun setI(gain: Double): Error {
        _i = gain
        return Error.kOk
    }

    override fun periodic() {
        _mockController.setpoint = _reference
        val measurement: Double =
            if (controlType) _feedbackSensor.position else _feedbackSensor.velocity
        device.set(
            when (_controlType) {
                ControlType.kDutyCycle -> _reference
                ControlType.kVelocity, ControlType.kPosition -> _mockController.calculate(
                    measurement
                )
                ControlType.kVoltage -> _reference / 12.0
                ControlType.kSmartMotion -> TODO()
                ControlType.kCurrent -> TODO()
                ControlType.kSmartVelocity -> TODO()
            }
        )
    }
}