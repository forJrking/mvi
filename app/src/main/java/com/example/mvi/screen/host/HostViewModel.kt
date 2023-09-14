package com.example.mvi.screen.host

import com.arch.mvi.viewmodel.BaseViewModel
import com.example.mvi.repo.DataRepository
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
                    val contacts = repo.fetchData()
                    HostState.Data(contacts)
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