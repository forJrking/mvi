package com.example.study.cause

import androidx.compose.ui.test.junit4.createComposeRule
import com.example.study.screen.ContactDetailsScreen
import com.example.study.framework.base.BaseTester
import com.example.study.framework.extensions.AND
import com.example.study.framework.extensions.GIVEN
import com.example.study.screen.compose.NavGraphs
import com.example.study.ui.theme.MVIHiltTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import io.github.kakaocup.compose.node.element.ComposeScreen
import org.junit.Rule
import org.junit.Test

class WechatContactTester : BaseTester() {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testLogonScreenWhenClickLogon() = run {
        GIVEN("start") {
            composeTestRule.setContent {
                MVIHiltTheme {
                    DestinationsNavHost(navGraph = NavGraphs.root)
                }
            }
        }
        AND("check") {
            ComposeScreen.onComposeScreen<ContactDetailsScreen>(composeTestRule) {
                button {
                    assertIsDisplayed()
                }
            }
        }
        step("snapshot") {
            compareScreenshot(composeTestRule)
        }
    }
}