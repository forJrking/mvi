package com.example.study.framework.base

import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.common.assertions.BaseAssertions

abstract class BaseScreen<out T : KScreen<T>> : KScreen<T>(), ScreenAssertions {
    override val layoutId: Int? = null
    override val viewClass: Class<*>? = null
}

interface ScreenAssertions {
    val specificViews: List<BaseAssertions>

    fun assertIsDisplayed() {
        specificViews.onEach { it.isDisplayed() }
    }

    fun assertIsNotDisplayed() {
        specificViews.onEach { it.isNotDisplayed() }
    }
}
