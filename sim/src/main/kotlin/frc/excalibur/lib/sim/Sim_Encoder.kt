package frc.excalibur.lib.sim

import com.revrobotics.CANEncoder as Encoder
import com.revrobotics.CANError as Error
import com.revrobotics.CANSparkMax as SparkMax

class Sim_Encoder(device: SparkMax, var rate: Double) : Encoder(device), Sim_Wrapper {
    private var _inverted = false
    private var _velocityConversion = 1.0
    private var _position = 0.0
    private var _velocity = 0.0
    private var _positionConversion = 1.0

    init {
        Sim_Wrapper.init(this)
    }

    override fun setInverted(inverted: Boolean): Error {
        _inverted = inverted
        return Error.kOk
    }

    override fun getInverted(): Boolean {
        return _inverted
    }

    override fun setVelocityConversionFactor(factor: Double): Error {
        _velocityConversion = factor
        return Error.kOk
    }

    override fun setPositionConversionFactor(factor: Double): Error {
        _positionConversion = factor
        return Error.kOk
    }

    override fun getPosition(): Double {
        return _position
    }

    override fun getVelocity(): Double {
        return _velocity
    }

    override fun setPosition(position: Double): Error {
        _position = position
        return Error.kOk
    }

    override fun getVelocityConversionFactor(): Double {
        return _velocityConversion
    }

    override fun getPositionConversionFactor(): Double {
        return _positionConversion
    }

    override fun periodic() {
    }
}
