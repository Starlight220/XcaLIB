package frc.excalibur.lib.command

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup

infix fun Command.andThen(command: Command): SequentialCommandGroup = this.andThen(command)