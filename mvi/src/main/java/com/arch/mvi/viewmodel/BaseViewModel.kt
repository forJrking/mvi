package com.arch.mvi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arch.mvi.intent.Action
import com.arch.mvi.model.Effect
import com.arch.mvi.model.State
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

/**
 * abstract BaseViewModel, viewModel继承与此类，需要定义Intent\State\Effect, 可约束和简化 View -> Intent -> ViewModel -> State\Effect -> View
 *
 * [I: Intent] Action 为了防止和Android的Intent()混淆
 * [M: State\Effect] 用于描述View的显示数据和状态, Effect用于描述SnackBar Toast Navigation 这些（热）事件
 *
 * [stateFlow] 需要默认初始化使用ShareFlow默认状态为null和LiveData一致通过下面方法转换
 * ``` kotlin
 * val stateFlow by lazy { _state.stateIn(viewModelScope, WhileSubscribed(), initial) }
 * ```
 * uiState聚合页面的全部UI状态的LiveData
 * ``` kotlin
 * val stateLiveData by lazy { state.asLiveData() }
 * ```
 */
abstract class BaseViewModel<A : Action, S : State, E : Effect> : ViewModel() {

    /**
     * [event]包含用户与ui的交互（如点击操作），也有来自后台的消息（如切换自习模式）
     */
    private val _action = Channel<A>()

    /**
     * [actor] 用于在非viewModelScope外使用
     */
    val actor: SendChannel<A> by lazy { _action }

    /**
     * [_state] 聚合页面的全部UI状态
     * 不需要默认值不使用 StateFlow:https://github.com/Kotlin/kotlinx.coroutines/issues/2515
     */
    private val _state = MutableSharedFlow<S>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val state: Flow<S> by lazy { _state.distinctUntilChanged() }

    /** [replayState] 重放当前uiState,replay始终是1 */
    val replayState
        get() = _state.replayCache.firstOrNull()

    /**
     * [effect]事件带来的副作用，通常是一次性事件 例如：弹Toast、导航Fragment等
     */
    private val _effect = MutableSharedFlow<E>()
    val effect: SharedFlow<E> by lazy { _effect.asSharedFlow() }

    /** 订阅事件的传入 onAction()分发处理事件 */
    init {
        viewModelScope.launch {
            _action.consumeAsFlow().collect {
                onAction(it, replayState)
            }
        }
    }

    fun sendAction(action: A) = viewModelScope.launch {
        _action.send(action)
    }

    protected abstract fun onAction(action: A, currentState: S?)

    protected fun emitState(builder: suspend () -> S?) = viewModelScope.launch {
        builder()?.let { _state.emit(it) }
    }

    protected fun emitEffect(builder: suspend () -> E?) = viewModelScope.launch {
        builder()?.let { _effect.emit(it) }
    }

    protected suspend fun emitState(state: S) = _state.emit(state)

    protected suspend fun emitEffect(effect: E) = _effect.emit(effect)

    /** 不挂起发送 state ，返回 boolean */
    protected fun tryEmitState(state: S) = _state.tryEmit(state)

    /**不挂起发送 effect ，返回 ChannelResult */
    protected fun tryEmitEffect(effect: E) = _effect.tryEmit(effect)
}
