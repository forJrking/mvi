package com.example.study.screen.host

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.arch.mvi.ext.observeWithLifecycle
import com.arch.mvi.view.BaseFragment
import com.example.study.databinding.FragmentHostListBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * A fragment representing a list of Items.
 */
@AndroidEntryPoint
class HostFragment : BaseFragment<FragmentHostListBinding, HostViewModel>() {

    private val args: HostFragmentArgs by navArgs()

    @Inject
    lateinit var renderer: RecyclerViewRenderer

    override val viewModel by viewModels<HostViewModel>()

    override fun initRenderers(binding: FragmentHostListBinding) {
        renderer.init(binding.list, args.columnCount) {
            viewModel.sendAction(HostAction.NavCompose(it))
        }
    }

    override fun observeStateOrEvent(viewModel: HostViewModel) {
        viewModel.state.observeWithLifecycle(this) {
            viewModel.state.collect {
                renderer.render(it)
            }
        }
        viewModel.effect.observeWithLifecycle(this) {
            findNavController().navigate(
                HostFragmentDirections.actionFragmentHostToFragmentCompose(
                    (it as HomeEvent.LaunchCompose).contact
                )
            )
        }
    }
}