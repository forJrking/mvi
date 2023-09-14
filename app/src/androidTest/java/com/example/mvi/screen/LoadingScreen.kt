package com.example.mvi.screen

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class LoadingScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<LoadingScreen>(semanticsProvider) {

    private val skeletonLayout: KNode = child {

    }

}
