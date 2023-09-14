package com.example.mvi.screen.logon

import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.arch.mvi.ext.observeWithLifecycle
import com.arch.mvi.view.BaseFragment
import com.example.mvi.databinding.FragmentLogonBinding
import com.example.mvi.ext.blockBackPressWithLiveData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map

@AndroidEntryPoint
class LogonFragment : BaseFragment<FragmentLogonBinding, LogonViewModel>() {

    override val viewModel: LogonViewModel by viewModels()

    override fun initRenderers(binding: FragmentLogonBinding) {
        binding.icon.setOnClickListener {
            viewModel.sendAction(LogOnAction.OnLogOnClicked)
        }
        binding.logonBtn.setOnClickListener {
            if (binding.logonBtn.text == "Success") {
                viewModel.sendAction(LogOnAction.Navigation)
            } else {
                viewModel.sendAction(LogOnAction.OnLogOnClicked)
            }
        }
    }

    override fun observeStateOrEvent(viewModel: LogonViewModel) {

        blockBackPressWithLiveData(viewModel.state.map { it !is LogOnState.Loading }.asLiveData())

        viewModel.effect.observeWithLifecycle(this) {
            when (it) {
                is LogOnEvent.NavigationToHost -> {
                    findNavController().navigate(
                        LogonFragmentDirections.actionFragmentLogonToFragmentHost(
                            it.num
                        )
                    )
                }
            }
        }

        viewModel.state.observeWithLifecycle(this) {
            binding.logonBtn.isEnabled = it !is LogOnState.Loading
            binding.logonBtn.text = when (it) {
                LogOnState.Loading -> "Loading"
                is LogOnState.Data -> "Success"
                LogOnState.Empty -> "Empty"
                else -> "Error"
            }
        }
    }
}