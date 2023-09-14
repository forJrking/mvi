package com.arch.mvi.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.viewbinding.ViewBinding
import com.arch.mvi.ext.withGenericViewBinding
import com.arch.mvi.viewmodel.BaseViewModel

/**
 * MVI - BaseFragment
 * */
abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel<*, *, *>> : Fragment() {

    protected abstract val viewModel: VM

    private var _binding: VB? = null

    protected val binding: VB
        get() = requireNotNull(_binding) { "The property of binding has been destroyed." }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = withGenericViewBinding(this::class.java) { clazz ->
            clazz.getMethod(
                "inflate",
                LayoutInflater::class.java,
                ViewGroup::class.java,
                Boolean::class.java
            ).invoke(null, inflater, container, false) as VB
        }
        return _binding?.run {
            initRenderers(this)
            root
        }
    }

    abstract fun initRenderers(binding: VB)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeStateOrEvent(viewModel)
        bindingLifecycle(viewLifecycleOwner.lifecycle)
    }

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
