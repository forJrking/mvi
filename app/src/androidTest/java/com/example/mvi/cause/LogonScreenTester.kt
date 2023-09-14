package com.example.mvi.cause

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import com.example.mvi.MainActivity
import com.example.mvi.framework.base.BaseTester
import com.example.mvi.framework.mock.MockNetworkTestRule
import com.example.mvi.framework.extensions.AND
import com.example.mvi.framework.extensions.GIVEN
import com.example.mvi.framework.extensions.THEN
import com.example.mvi.framework.extensions.WHEN
import com.example.mvi.screen.ContactListScreen
import com.example.mvi.screen.LogonScreen
import com.kaspersky.kaspresso.testcases.core.testcontext.BaseTestContext
import okhttp3.mockwebserver.QueueDispatcher
import org.junit.Rule
import org.junit.Test

class LogonScreenTester : BaseTester() {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var scenario: ActivityScenario<MainActivity>

    override fun afterEach(): BaseTestContext.() -> Unit = {
        scenario.moveToState(Lifecycle.State.DESTROYED)
    }

    @Test
    fun testLogonSuccessContactListShown() = runSnapshot {
        GIVEN("Customer launch app") {
            MockNetworkTestRule.setDispatcher(QueueDispatcher())
            scenario = ActivityScenario.launch(MainActivity::class.java)
        }
        WHEN("Customer click logon button") {
            LogonScreen {
                assertIsDisplayed()
                buttonClick()
                checkLogonState("Success")
                snapshot("customerClickedLogonSuccess")
            }
        }
        AND("Customer logon to contact") {
            LogonScreen {
                buttonClick()
            }
        }
        THEN("The contact list is displayed") {
            ContactListScreen {
                assertIsDisplayed()
                dataListShown()
            }
        }
    }
}