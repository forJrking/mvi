package com.example.mvi.framework.base

import androidx.compose.ui.test.SemanticsNodeInteraction
import com.example.mvi.framework.mock.MockNetworkTestRule
import com.karumi.shot.ScreenshotTest
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.kaspersky.kaspresso.testcases.api.testcaserule.TestCaseRule
import com.kaspersky.kaspresso.testcases.core.testcontext.BaseTestContext
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import org.junit.Rule

interface MockTestRule {
    fun beforeEachTest()
    fun afterEachTest()
}

abstract class BaseTester : TestCase(), ScreenshotTest {

    protected val mockTestRules = listOf(
        MockNetworkTestRule
    )

    protected open fun beforeEach(): BaseTestContext.() -> Unit = {}
    protected open fun afterEach(): BaseTestContext.() -> Unit = {}

    @get:Rule
    val testCaseRule = TestCaseRule(
        testClassName = javaClass.simpleName,
        kaspressoBuilder = Kaspresso.Builder.withComposeSupport() {
//            screenshots =
            beforeEachTest {
                mockTestRules.onEach { it.beforeEachTest() }
                beforeEach().invoke(this)
            }
            afterEachTest {
                mockTestRules.onEach { it.afterEachTest() }
                afterEach().invoke(this)
            }
        }
    )

    /**ScreenShot Ui Test*/
    fun snapshot(name: String? = null) =
        this.device.activities.getResumed()?.let {
            compareScreenshot(activity = it, name = name)
        }

    /**Custom Ui Test*/
    fun run(steps: TestContext<Unit>.() -> Unit) = testCaseRule.run(steps = steps)

    /**ScreenShot Ui Test*/
    fun runSnapshot(name: String? = null, steps: TestContext<Unit>.() -> Unit) = testCaseRule.run {
        apply {
            steps()
            step("Screenshot of currently resumed activity") {
                this.device.activities.getResumed()?.let {
                    compareScreenshot(activity = it, name = name)
                }
            }
        }
    }

    /**ScreenShot Compose Test*/
    fun snapshotCompose(
        node: SemanticsNodeInteraction,
        name: String? = null,
        steps: TestContext<Unit>.() -> Unit
    ) = testCaseRule.run {
        apply {
            steps()
            step("Screenshot of Composable") {
                compareScreenshot(node, name)
            }
        }
    }
}
