package com.example.mvi.screen.template

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.arch.mvi.intent.Action
import com.arch.mvi.model.Effect
import com.arch.mvi.model.State
import com.arch.mvi.view.StateEffectScaffold
import com.arch.mvi.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

/**
 * - new build package and dirs
 *   - contract
 *   - viewmodel
 *   - view
 */

/** - contract */
sealed class LogonAction : Action {
    data object OnButtonClicked : LogonAction()
}

sealed class LogonState : State {
    data object LogonHub : LogonState()
    data object Loading : LogonState()
    data object Error : LogonState()
}

sealed class LogonEffect : Effect {
    data object Navigate : LogonEffect()
}

/** - viewmodel */
class LogonViewModel : BaseViewModel<LogonAction, LogonState, LogonEffect>() {
    override fun onAction(action: LogonAction, currentState: LogonState?) {
        when (action) {
            LogonAction.OnButtonClicked -> {
                flow {
                    kotlinx.coroutines.delay(2000)
                    emit(Unit)
                }.onStart {
                    emitState(LogonState.Loading)
                }.onEach {
                    emitEffect(LogonEffect.Navigate)
                }.catch {
                    emitState(LogonState.Error)
                }.launchIn(viewModelScope)
            }
        }
    }
}

/** - view */
@Preview
@Composable
fun LogonScreen() {
    val scaffoldState = rememberScaffoldState()
    StateEffectScaffold(viewModel = hiltViewModel<LogonViewModel>(),
        initialState = LogonState.LogonHub,
        sideEffect = { viewModel, sideEffect ->
            when (sideEffect) {
                LogonEffect.Navigate -> {
                    scaffoldState.snackbarHostState.showSnackbar("navigate")
                }
            }
        }
    ) { viewModel, state ->
        Scaffold(
            scaffoldState = scaffoldState
        ) {
            Box(modifier = Modifier.fillMaxSize().padding(it), contentAlignment = Alignment.Center) {
                when (state) {
                    LogonState.Loading -> CircularProgressIndicator()
                    LogonState.Error -> Text(text = "error")
                    LogonState.LogonHub -> Button(onClick = {
                        viewModel.sendAction(LogonAction.OnButtonClicked)
                    }) {
                        Text(text = "logon")
                    }
                }
            }
        }
    }
}
