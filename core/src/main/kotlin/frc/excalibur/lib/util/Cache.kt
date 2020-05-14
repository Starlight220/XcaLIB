package frc.excalibur.lib.util

/**
 * A "cache" to create variables similar to C local static variables.
 * Mainly useful if you want to not have a property (for visibility reasons, etc.) to keep a value,
 * but do not want to calculate it each use.
 */
open class Cache {
    private val map = mutableMapOf<String, Any?>()

    /**
     * Caches the value. If a variable under the same handle has been cached, this will return it.
     * Otherwise, will calculate the data, store it, and return it.
     *
     * @param T the type of the cached value
     * @param handle the "key" under which the value is saved
     * @param data a lambda to compute the value, will be called only if there is the cached value
     * doesn't exist or is of a different type.
     * @return the cached value, or if it doesn't exist it will return the result of [data]
     */
    inline operator fun <reified T> invoke(handle: String, noinline data: () -> T): T {
        val value = cache(handle, data)
        return if(value is T) value else data()
    }

    /**
     * Caches the value. If a variable under the same handle has been cached, this will return it.
     * Otherwise, will calculate the data, store it, and return it.
     *
     * @param T the type of the cached value
     * @param handle the "key" under which the value is saved
     * @param data a lambda to compute the value, will be called only if the
     * @return the cached value, **might not be of type [T]** - if the previous value was of a
     * different type. If you want to get null in case of a wrong type, use [invoke].
     */
    fun <T> cache(handle: String, data: () -> T): Any {
        return map.getOrPut(key = handle, defaultValue = data)
    }

    /**
     * The default cache.
     * For big caches, create a [Cache] object in order to remove any chance of two sources
     * overwriting the same key.
     */
    companion object Default : Cache()
}

/**
 * Internal for now, under development.
 */
internal class SingletonCache<T> {
    private var array = arrayOfNulls<Any>(1)

    /**
     * @throws ClassCastException if there is a
     */
    operator fun invoke(data: () -> T): T {
        if (array[0] == null) {
            array[0] = data()
        }
        @Suppress("UNCHECKED_CAST")
        return array[0] as T
    }
}