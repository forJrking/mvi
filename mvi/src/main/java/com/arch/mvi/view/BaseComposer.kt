package com.arch.mvi.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.arch.mvi.model.Effect
import com.arch.mvi.model.State
import com.arch.mvi.viewmodel.BaseViewModel
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@Composable
fun <S : State, E : Effect, VM : BaseViewModel<*, S, E>> StateEffectScaffold(
    viewModel: VM,
    initialState: S? = null,
    lifecycle: Lifecycle = LocalLifecycleOwner.current.lifecycle,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    context: CoroutineContext = EmptyCoroutineContext,
    sideEffect: (suspend (VM, E) -> Unit)? = null,
    content: @Composable (VM, S) -> Unit
) {
    sideEffect?.let {
        val lambdaEffect by rememberUpdatedState(sideEffect)
        LaunchedEffect(viewModel.effect, lifecycle, minActiveState) {
            lifecycle.repeatOnLifecycle(minActiveState) {
                if (context == EmptyCoroutineContext) {
                    viewModel.effect.collect { lambdaEffect(viewModel, it) }
                } else withContext(context) {
                    viewModel.effect.collect { lambdaEffect(viewModel, it) }
                }
            }
        }
    }
    // collectAsStateWithLifecycle 在横竖屏变化时会先回调initialState 所以必须把replay latest state传递过去
    val uiState = viewModel.state.collectAsStateWithLifecycle(
        initialValue = viewModel.replayState,
        lifecycle = lifecycle,
        minActiveState = minActiveState,
        context = context
    )
    (uiState.value ?: initialState)?.let { content(viewModel, it) }
}
