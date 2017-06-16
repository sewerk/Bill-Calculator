@file:JvmName("Whitebox")

package pl.srw.billcalculator

import java.lang.reflect.Field

fun Any.invokeHiddenMethod(name: String) {
    val method = this.javaClass.getDeclaredMethod(name)
    method.isAccessible = true
    method.invoke(this)
}

fun <T> Any.getState(name: String): T {
    return getInternalState(this, name)
}

fun Any.setState(name: String, value: Any) {
    setInternalState(this, name, value)
}

@Suppress("UNCHECKED_CAST")
fun <T> getInternalState(target: Any, field: String): T {
    val c = target.javaClass
    try {
        val f = getFieldFromHierarchy(c, field)
        f.isAccessible = true
        return f.get(target) as T
    } catch (e: Exception) {
        throw RuntimeException("Unable to get internal state on a private field. Please report to mockito mailing list.", e)
    }

}

fun setInternalState(target: Any, field: String, value: Any) {
    val c = target.javaClass
    try {
        val f = getFieldFromHierarchy(c, field)
        f.isAccessible = true
        f.set(target, value)
    } catch (e: Exception) {
        throw RuntimeException("Unable to set internal state on a private field. Please report to mockito mailing list.", e)
    }

}

private fun getFieldFromHierarchy(clazz: Class<*>, field: String): Field {
    var vClazz = clazz
    var f = getField(vClazz, field)
    while (f == null && vClazz != Any::class.java) {
        vClazz = vClazz.superclass
        f = getField(vClazz, field)
    }
    if (f == null) {
        throw RuntimeException(
                "You want me to get this field: '" + field +
                        "' on this class: '" + vClazz.simpleName +
                        "' but this field is not declared withing hierarchy of this class!")
    }
    return f
}

private fun getField(clazz: Class<*>, field: String): Field? {
    try {
        return clazz.getDeclaredField(field)
    } catch (e: NoSuchFieldException) {
        return null
    }

}
