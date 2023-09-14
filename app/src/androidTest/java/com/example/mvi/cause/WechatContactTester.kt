package com.example.mvi.cause

import androidx.compose.ui.test.junit4.createComposeRule
import com.example.mvi.screen.ContactDetailsScreen
import com.example.mvi.framework.base.BaseTester
import com.example.mvi.framework.extensions.AND
import com.example.mvi.framework.extensions.GIVEN
import com.example.mvi.screen.compose.NavGraphs
import com.example.mvi.ui.theme.MVIHiltTheme
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