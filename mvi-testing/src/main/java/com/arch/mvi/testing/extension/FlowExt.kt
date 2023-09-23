package com.arch.mvi.testing.extension

import com.arch.mvi.model.Effect
import com.arch.mvi.model.State
import com.arch.mvi.viewmodel.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher


fun <S : State> BaseViewModel<*, S, *>.stateCollection(testScope: TestScope) = state.asTestList(testScope)


fun <E : Effect> BaseViewModel<*, *, E>.effectCollection(testScope: TestScope) = effect.asTestList(testScope)

/**flow testing function*/
fun <T> Flow<T>.asTestList(testScope: TestScope): List<T> = mutableListOf<T>().also { values ->
    testScope.launchTest {
        this@asTestList.toList(values)
    }
}

fun TestScope.launchTest(block: suspend CoroutineScope.() -> Unit) = this.backgroundScope.launch(
    context = UnconfinedTestDispatcher(this.testScheduler), block = block
)
