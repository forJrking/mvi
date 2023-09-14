package com.example.study.screen.logon

import com.arch.mvi.intent.Action
import com.arch.mvi.model.Effect
import com.arch.mvi.model.State

sealed class LogOnAction : Action {
    object OnLogOnClicked : LogOnAction()
    object Navigation : LogOnAction()
}

sealed class LogOnEvent : Effect {
    data class NavigationToHost(val num: Int) : LogOnEvent()
}

sealed class LogOnState : State {
    object Loading : LogOnState()
    object Empty : LogOnState()
    data class Data(val num: Int) : LogOnState()
    data class Error(val ex: Throwable) : LogOnState()
}