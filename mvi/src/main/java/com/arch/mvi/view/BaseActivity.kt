package com.arch.mvi.view

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.viewbinding.ViewBinding
import com.arch.mvi.ext.withGenericViewBinding
import com.arch.mvi.viewmodel.BaseViewModel

abstract class BaseActivity<VB : ViewBinding, VM : BaseViewModel<*, *, *>> : AppCompatActivity() {

    protected abstract val viewModel: VM

    lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = withGenericViewBinding(this::class.java) { clazz ->
            clazz.getMethod("inflate", LayoutInflater::class.java)
                .invoke(null, LayoutInflater.from(this)) as VB
        }
        initRenderers(binding)
        setContentView(binding.root)
        observeStateOrEvent(viewModel)
        bindingLifecycle(lifecycle)
    }

    abstract fun initRenderers(binding: VB)

    abstract fun observeStateOrEvent(viewModel: VM)

    private fun bindingLifecycle(lifecycle: Lifecycle) {
        // BaseRenderer must be public, `getFields()` improve performance
        for (field in this::class.java.fields) {
            val value = field.get(this)
            if (value is LifecycleObserver) {
                lifecycle.addObserver(value)
            }
        }
    }
}