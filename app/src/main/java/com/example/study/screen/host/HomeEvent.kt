package com.example.study.screen.host

import com.arch.mvi.model.Effect
import com.example.study.repo.Contact

sealed class HomeEvent : Effect {
    data class LaunchCompose(val contact: Contact) : HomeEvent()
}