@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.mvi.core

import com.arch.mvi.model.Effect
import com.arch.mvi.model.State
import com.arch.mvi.viewmodel.BaseViewModel
import com.example.mvi.di.CoroutineDispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.MockitoAnnotations


@ExtendWith(
    value = [
        CoroutineTestExtension::class,
        InstantTaskExecutorExtension::class
    ]
)
open class BaseTester {

    protected val testDispatcher: TestDispatcher
        get() = CoroutineTestExtension.testDispatcher

    protected val testDispatcherProvider: CoroutineDispatcherProvider =
        TestCoroutineDispatcherProvider(testDispatcher)

    private lateinit var openMocks: AutoCloseable

    @BeforeEach
    open fun setUp() {
        openMocks = MockitoAnnotations.openMocks(this)
    }

    @AfterEach
    open fun tearDown() {
        openMocks.close()
    }
}


internal fun <S : State> BaseViewModel<*, S, *>.stateCollection(testScope: TestScope) =
    this.state.toListTest(testScope)


internal fun <E : Effect> BaseViewModel<*, *, E>.effectCollection(testScope: TestScope) =
    this.effect.toListTest(testScope)

/**flow testing function*/
internal fun <T> Flow<T>.toListTest(testScope: TestScope): List<T> {
    return mutableListOf<T>().also { values ->
        testScope.launchTest {
            this@toListTest.toList(values)
        }
    }
}

internal fun TestScope.launchTest(block: suspend CoroutineScope.() -> Unit) =
    this.backgroundScope.launch(
        context = UnconfinedTestDispatcher(this.testScheduler),
        block = block
    )


