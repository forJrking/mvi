package com.example.mvi.screen.logon

import androidx.lifecycle.viewModelScope
import com.arch.mvi.viewmodel.BaseViewModel
import com.example.mvi.di.LogonRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class LogonViewModel @Inject constructor(
    private val repo: LogonRepo
) : BaseViewModel<LogOnAction, LogOnState, LogOnEvent>() {

    override fun onAction(action: LogOnAction, currentState: LogOnState?) {
        when (action) {
            LogOnAction.OnLogOnClicked -> logon()
            LogOnAction.Navigation -> {
                //必须在登陆成功获取到数字时候才能跳转
                if (currentState is LogOnState.Data) {
                    emitEffect { LogOnEvent.NavigationToHost(currentState.num) }
                }
            }
        }
    }

    private fun logon() {
        repo.getRandomNumber()
            .onStart {
                emitState(LogOnState.Loading)
            }.catch { ex ->
                emitState(LogOnState.Error(ex))
            }.onEach { result ->
                if (result == 0) {
                    emitState(LogOnState.Empty)
                } else {
                    emitState(LogOnState.Data(result))
                }
            }.launchIn(viewModelScope)
    }
}