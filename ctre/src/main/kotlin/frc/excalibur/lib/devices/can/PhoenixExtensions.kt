package frc.excalibur.lib.devices.can

import com.ctre.phoenix.motorcontrol.InvertType
import com.ctre.phoenix.motorcontrol.can.TalonSRX as Talon
import com.ctre.phoenix.motorcontrol.can.VictorSPX as Victor

/**
 * Follows another TalonSRX, in the same direction.
 * @receiver the follower/slave controller
 * @param master the leader/master controller
 */
infix fun Talon.follows(master: Talon): Talon {
    this.apply {
        follow(master)
        setInverted(InvertType.FollowMaster)
    }
    return this
}

/**
 * Follows another TalonSRX, in the opposite direction.
 * @receiver the follower/slave controller
 * @param master the leader/master controller
 */
infix fun Talon.oppose(master: Talon): Talon {
    this.apply {
        follow(master)
        setInverted(InvertType.OpposeMaster)
    }
    return this
}

/**
 * Follows another VictorSPX, in the same direction.
 * @receiver the follower/slave controller
 * @param master the leader/master controller
 */
infix fun Victor.follows(master: Victor): Victor {
    this.apply {
        follow(master)
        setInverted(InvertType.FollowMaster)
    }
    return this
}

/**
 * Follows another VictorSPX, in the opposite direction.
 * @receiver the follower/slave controller
 * @param master the leader/master controller
 */
infix fun Victor.opposes(master: Victor): Victor {
    this.apply {
        follow(master)
        setInverted(InvertType.OpposeMaster)
    }
    return this
}

/**
 * Follows another TalonSRX, in the same direction.
 * @receiver the follower/slave controller
 * @param master the leader/master controller
 */
infix fun Victor.follows(master: Talon): Victor {
    this.apply {
        follow(master)
        setInverted(InvertType.FollowMaster)
    }
    return this
}

/**
 * Follows another TalonSRX, in the opposite direction.
 * @receiver the follower/slave controller
 * @param master the leader/master controller
 */
infix fun Victor.opposes(master: Talon): Victor {
    this.apply {
        follow(master)
        setInverted(InvertType.OpposeMaster)
    }
    return this
}
