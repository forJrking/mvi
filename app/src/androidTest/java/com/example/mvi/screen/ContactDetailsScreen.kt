package com.example.mvi.screen

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import com.example.mvi.test.TestTag
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class ContactDetailsScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<ContactDetailsScreen>(
        semanticsProvider = semanticsProvider,
    ) {

    val button: KNode = child {
        hasTestTag(TestTag.CIRCLEOFFRIENDS)
    }
}
