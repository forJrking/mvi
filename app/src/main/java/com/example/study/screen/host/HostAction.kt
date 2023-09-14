package com.example.study.screen.host

import com.arch.mvi.intent.Action
import com.example.study.repo.Contact

sealed class HostAction : Action {

    object LoadData : HostAction()
    data class NavCompose(val contact: Contact) : HostAction()
}