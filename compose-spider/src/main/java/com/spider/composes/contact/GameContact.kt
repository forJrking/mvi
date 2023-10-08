package com.spider.composes.contact

import com.arch.mvi.intent.Action
import com.arch.mvi.model.Effect
import com.arch.mvi.model.State

/** - contract */
sealed class GameAction : Action {
    data object LoadData : GameAction()
    data object MoveCard : GameAction()
}

sealed class GameState : State {
    data object Loading : GameState()
    data object Empty : GameState()
    data class Deal(val title: String) : GameState()
}

sealed class GameEffect : Effect {
    data class ShowToast(val content: String) : GameEffect()
}
