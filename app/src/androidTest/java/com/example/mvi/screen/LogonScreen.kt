package com.example.mvi.screen

import com.example.mvi.R
import com.example.mvi.framework.base.BaseScreen
import io.github.kakaocup.kakao.text.KButton

object LogonScreen : BaseScreen<LogonScreen>() {

    private val logonButton: KButton = KButton { withId(R.id.logon_btn) }

    override val specificViews = listOf(logonButton)

    fun buttonClick() = logonButton.click()

    fun checkLogonState(txt: String) {
        logonButton {
            hasText(txt)
        }
    }
}
