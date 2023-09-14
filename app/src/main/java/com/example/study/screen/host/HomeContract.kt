package com.example.study.screen.host

import com.arch.mvi.intent.Action
import com.arch.mvi.model.Effect
import com.arch.mvi.model.State
import com.example.study.repo.Contact

sealed class HostAction : Action {

    object LoadData : HostAction()
    data class NavCompose(val contact: Contact) : HostAction()
}

sealed interface HostState : State {

    object Loading : HostState
    object Empty : HostState
    object None : HostState

    data class Data(val data: List<Contact>) : HostState
}

sealed class HomeEvent : Effect {
    data class LaunchCompose(val contact: Contact) : HomeEvent()
}