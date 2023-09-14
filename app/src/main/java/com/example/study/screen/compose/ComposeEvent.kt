package com.example.study.screen.compose

import com.arch.mvi.model.Effect

sealed class ComposeEvent : Effect {
    data object ShowWarring : ComposeEvent()
    data object NavDetails : ComposeEvent()
}