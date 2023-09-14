package com.example.study.screen.host

import com.arch.mvi.viewmodel.BaseViewModel
import com.example.study.repo.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HostViewModel @Inject constructor(
    private val repo: DataRepository
) : BaseViewModel<HostAction, HostState, HomeEvent>() {

    init {
        sendAction(HostAction.LoadData)
    }

    override fun onAction(action: HostAction, currentState: HostState?) {
        when (action) {
            HostAction.LoadData -> {
                emitState {
                    HostState.Data(repo.fetchData())
                }
            }
            is HostAction.NavCompose -> {
                emitEffect {
                    HomeEvent.LaunchCompose(action.contact)
                }
            }
        }
    }
}