package com.spider.composes.viewmodel

import com.arch.mvi.viewmodel.BaseViewModel
import com.spider.composes.contact.GameAction
import com.spider.composes.contact.GameEffect
import com.spider.composes.contact.GameState
import com.spider.composes.di.CoroutineDispatcherProvider
import com.spider.composes.model.reducerModule
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    includes(reducerModule)
    viewModelOf(::GameViewModel)
}

/** - viewmodel */
class GameViewModel constructor(
//    private val reducer: GameReducer,
//    private val repository: GameRepository,
    private val dispatcherProvider: CoroutineDispatcherProvider,
) : BaseViewModel<GameAction, GameState, GameEffect>() {

    init {
        sendAction(GameAction.LoadData)
    }

    override fun onAction(action: GameAction, currentState: GameState?) {
//        when (action) {
//            GameAction.LoadData -> {
//                /*viewModelScope.launch {
//                    withContext(dispatcherProvider.io()) {
//                        runCatching { repository.fetchRemoteOrLocalData() }
//                    }.onSuccess {
//                        emitState(reducer.reduceRemoteOrLocalData())
//                    }.onFailure {
//                        emitState(GameState.Empty)
//                    }
//                }*/
//            }
//
//            is GameAction.OnButtonClicked -> {
//                emitEffect { GameEffect.ShowToast("Clicked ${action.id}") }
//            }
//        }
    }
}
