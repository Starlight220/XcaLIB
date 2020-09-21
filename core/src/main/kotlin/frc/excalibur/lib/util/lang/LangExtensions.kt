package frc.excalibur.lib.util.lang

import edu.wpi.first.wpilibj.Filesystem
import java.nio.file.Path

/**
 *  A postfix function to have a singleton array. Useful to convert to java varargs, as their
 * transition to Kotlin can be buggy/inconvenient.
 */
inline fun <reified T> T.asArray(): Array<T> = arrayOf(this)

/**
 *
 */
/**
 *  Resolves this string as a file from the deploy directory.
 * @see [Filesystem.getDeployDirectory]
 */
fun String.asDeployPath(): Path = Filesystem.getDeployDirectory()
    .toPath()
    .resolve(this)
