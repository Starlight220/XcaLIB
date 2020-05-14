package frc.excalibur.lib.util.lang

import edu.wpi.first.wpilibj.Filesystem
import java.nio.file.Path

inline fun <reified T> T.asArray() = arrayOf(this)


fun String.asDeployPath(): Path = Filesystem.getDeployDirectory()
        .toPath()
        .resolve(this)

