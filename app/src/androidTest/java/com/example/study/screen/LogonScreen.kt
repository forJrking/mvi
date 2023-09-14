package com.example.study.screen

import com.example.study.R
import com.example.study.framework.base.BaseScreen
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
