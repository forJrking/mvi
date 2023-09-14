package com.example.study.ext

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData

class BackPressedBlocker(private val onAction: () -> Unit) : OnBackPressedCallback(true) {
    override fun handleOnBackPressed() {
        // block, so do nothing
        onAction()
    }
}

/**
 * 在loading时候的某些情况下需要block返回键
 * 大部分时候不需要block用户操作, api请求回自动cancel
 */
fun Fragment.blockBackPressWithLiveData(liveData: LiveData<Boolean>, block: () -> Unit = {}) =
    BackPressedBlocker(block).also { blocker ->
        liveData.observe(viewLifecycleOwner) { enable ->
            if (enable) {
                blocker.remove()
            } else {
                blocker.remove()
                activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, blocker)
            }
        }
    }

internal fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("Permissions should be called in the context of an Activity")
}
