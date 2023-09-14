package com.arch.mvi.ext

import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType


internal fun <V : ViewBinding?> withGenericViewBinding(
    aClass: Class<*>, block: (Class<V>) -> V
): V {
    return try {
        val genericSuperclass = aClass.genericSuperclass
        if (genericSuperclass is ParameterizedType) {
            genericSuperclass.actualTypeArguments.forEach {
                val result: Class<*>
                try {
                    result = it as Class<V>
                } catch (_: Exception) {
                    return@forEach
                }
                if (ViewBinding::class.java.isAssignableFrom(result)) {
                    return block.invoke(result)
                }
            }
        }
        withGenericViewBinding(aClass.superclass, block)
    } catch (e: Exception) {
        e.printStackTrace()
        throw IllegalArgumentException("There is no generic of ViewBinding.")
    }
}
