package com.example.study.screen.template


import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.arch.mvi.intent.Action
import com.arch.mvi.model.Effect
import com.arch.mvi.model.State
import com.arch.mvi.view.StateEffectScaffold
import com.arch.mvi.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * - new build package and dirs
 *   - contract
 *   - viewmodel
 *   - view
 */

/** - contract */
sealed class TemplateAction : Action {
    data object LoadData : TemplateAction()
    data class OnButtonClicked(val id: Int) : TemplateAction()
}

sealed class TemplateState : State {
    data object Loading : TemplateState()
    data object Empty : TemplateState()
}

sealed class TemplateEffect : Effect {
    data class ShowToast(val content: String) : TemplateEffect()
}

/** - viewmodel */
@HiltViewModel
class TemplateViewModel @Inject constructor(
//    private val reducer: TemplateReducer,
//    private val repository: TemplateRepository,
//    private val dispatcherProvider: CoroutineDispatcherProvider
) : BaseViewModel<TemplateAction, TemplateState, TemplateEffect>() {

    init {
        sendAction(TemplateAction.LoadData)
    }

    override fun onAction(action: TemplateAction, currentState: TemplateState?) {
        when (action) {
            TemplateAction.LoadData -> {
                /*viewModelScope.launch {
                    withContext(dispatcherProvider.io()) {
                        runCatching { repository.fetchRemoteOrLocalData() }
                    }.onSuccess {
                        emitState(reducer.reduceRemoteOrLocalData())
                    }.onFailure {
                        emitState(TemplateState.Empty)
                    }
                }*/
            }

            is TemplateAction.OnButtonClicked -> {
                emitEffect { TemplateEffect.ShowToast("Clicked ${action.id}") }
            }
        }
    }
}

/** - view */
@Composable
fun TemplateScreen() {
    StateEffectScaffold(
        viewModel = hiltViewModel<TemplateViewModel>(),
        sideEffect = { viewModel, sideEffect ->
            when (sideEffect) {
                is TemplateEffect.ShowToast -> {
                    TODO("ShowToast ${sideEffect.content}")
                }
            }
        }
    ) { viewModel, state ->
        when (state) {
            TemplateState.Loading -> {
                TODO("Loading")
            }

            TemplateState.Empty -> {
                TODO("Empty")
            }
        }
    }
}

