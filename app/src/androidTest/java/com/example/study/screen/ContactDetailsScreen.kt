package com.example.study.screen

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import com.example.study.test.TestTag
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
