package com.example.study.screen.compose

import android.net.Uri
import com.arch.mvi.intent.Action

sealed class ComposeAction : Action {
    data class PickImages(val uri: List<Uri>) : ComposeAction()
    data class WipeImage(val uri: Uri) : ComposeAction()
    data class LoadData(val contactId: String) : ComposeAction()
    data object OnExpendClicked : ComposeAction()
    data object OnActionClicked : ComposeAction()
    data object OnFABClicked : ComposeAction()
}