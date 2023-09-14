package com.example.study.screen.compose

import android.net.Uri
import com.arch.mvi.model.State

sealed class ComposeState : State {
    data object Loading : ComposeState()
    data object Empty : ComposeState()
    data class Chat(
        val uri: List<Uri> = emptyList(),
        val content: String,
        val expend: Boolean
    ) : ComposeState()

    companion object {
        const val MAX_COUNT = 6
    }
}
