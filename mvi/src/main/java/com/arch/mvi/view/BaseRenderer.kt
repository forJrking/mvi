package com.arch.mvi.view

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

abstract class BaseRenderer(
    val disposeEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
) : LifecycleEventObserver {

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == disposeEvent) clear()
    }

    abstract fun clear()
}
