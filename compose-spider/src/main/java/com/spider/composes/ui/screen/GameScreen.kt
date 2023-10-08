package com.spider.composes.ui.screen

import androidx.compose.runtime.Composable
import com.arch.mvi.view.StateEffectScaffold
import com.spider.composes.contact.GameEffect
import com.spider.composes.contact.GameState
import com.spider.composes.viewmodel.GameViewModel
import org.koin.androidx.compose.koinViewModel

/** - view */
@Composable
fun GameScreen() {
    StateEffectScaffold(viewModel = koinViewModel<GameViewModel>(), sideEffect = { viewModel, sideEffect ->
        when (sideEffect) {
            is GameEffect.ShowToast -> {
                TODO("ShowToast ${sideEffect.content}")
            }
        }
    }) { viewModel, state ->
        when (state) {
            GameState.Loading -> {
                TODO("Loading")
            }

            GameState.Empty -> {
                TODO("Empty")
            }
        }
    }
}
