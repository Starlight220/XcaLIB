package frc.excalibur.lib.devices.can

import edu.wpi.first.wpilibj.Compressor
import edu.wpi.first.wpilibj.DoubleSolenoid
import edu.wpi.first.wpilibj.Solenoid


typealias PCM = PneumaticsControlModule
class PneumaticsControlModule private constructor(private val canID : Int = 0) {
    init {
        instances[canID] = this
    }
    val compressor : Compressor = Compressor(canID)
    val pressureSwitch : Boolean
        get() = compressor.pressureSwitchValue


    companion object{
        private val instances : MutableMap<Int, PCM> = mutableMapOf()
        operator fun invoke(canID : Int = 0) : PCM = instances.getOrPut(canID){ PCM(canID) }
    }

    fun DoubleSolenoid(fwd : Int, rev : Int) : DoubleSolenoid = DoubleSolenoid(canID, fwd, rev)
    fun SingleSolenoid(port : Int) : Solenoid = Solenoid(canID, port)
}

var DoubleSolenoid.state : Boolean
    get() = get() == DoubleSolenoid.Value.kForward
    set(value) = set(if(value) DoubleSolenoid.Value.kForward else DoubleSolenoid.Value.kReverse)
