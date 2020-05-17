package frc.excalibur.lib.sim

import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMaxLowLevel.MotorType
import edu.wpi.first.wpilibj.RobotBase
import sim.Sim_SparkMax

interface Sim_Wrapper {
    companion object {
        private val devices: Set<Sim_Wrapper> = mutableSetOf()

        fun init(device: Sim_Wrapper) {
            devices + device
        }

        fun runPeriodic() {
            devices.forEach(Sim_Wrapper::periodic)
        }

        fun getSparkMax(id: Int, posOrVel: Boolean): CANSparkMax {
            return if (RobotBase.isSimulation()) {
                Sim_SparkMax(id, posOrVel)
            } else {
                CANSparkMax(id, MotorType.kBrushless)
            }
        }
    }

    fun periodic()
}