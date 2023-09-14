package com.example.study.screen.host

import com.arch.mvi.model.State
import com.example.study.repo.Contact

sealed interface HostState : State {

    object Loading : HostState
    object Empty : HostState
    object None : HostState

    data class Data(val data: List<Contact>) : HostState
}