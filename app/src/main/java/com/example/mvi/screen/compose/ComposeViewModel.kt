package com.example.mvi.screen.compose

import android.net.Uri
import android.util.Log
import com.arch.mvi.viewmodel.BaseViewModel
import com.example.mvi.di.CoroutineDispatcherProvider
import com.example.mvi.repo.DataRepository
import com.example.mvi.screen.compose.ComposeState.Companion.MAX_COUNT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ComposeViewModel @Inject constructor(
    private val repo: DataRepository,
    private val dispatcherProvider: CoroutineDispatcherProvider
) : BaseViewModel<ComposeAction, ComposeState, ComposeEvent>() {

//    init {
//        sendAction(ComposeAction.LoadData)
//    }

    override fun onAction(action: ComposeAction, currentState: ComposeState?) {
        when (action) {
            is ComposeAction.LoadData -> emitState {
                emitState(ComposeState.Loading)
                reduceRemoteData(repo.fetchWeChat(action.contactId))
            }

            is ComposeAction.PickImages -> {
                emitState {
                    reducePickImages(
                        uri = action.uri,
                        state = currentState as? ComposeState.Chat
                    )
                }
            }

            is ComposeAction.WipeImage -> {
                emitState {
                    reduceWipeImage(
                        uri = action.uri,
                        state = currentState as? ComposeState.Chat
                    )
                }
            }

            ComposeAction.OnExpendClicked -> {
                emitState {
                    (currentState as? ComposeState.Chat)?.copy(expend = !currentState.expend)
                }
            }

            ComposeAction.OnFABClicked -> emitEffect { ComposeEvent.ShowWarring }
            ComposeAction.OnActionClicked -> emitEffect { ComposeEvent.NavDetails }
        }
    }

    private suspend fun reduceWipeImage(uri: Uri, state: ComposeState.Chat?): ComposeState? {
        return withContext(dispatcherProvider.io()) {
            state?.uri?.toMutableList()?.let {
                state.copy(uri = it.apply { remove(uri) })
            }
        }
    }

    private suspend fun reducePickImages(uri: List<Uri>, state: ComposeState.Chat?): ComposeState? {
        return withContext(dispatcherProvider.io()) {
            if (uri.isNotEmpty() && state != null) {
                state.copy(
                    uri = state.uri.toMutableList().let {
                        if ((it.size + uri.size) > MAX_COUNT) {
                            it.addAll(0, uri)
                            it.subList(0, MAX_COUNT)
                        } else {
                            it += uri
                            it
                        }
                    }
                )
            } else null
        }
    }

    private fun reduceRemoteData(result: Result<String>): ComposeState {
        val content = result.getOrDefault("")
        return if (content.isEmpty()) {
            ComposeState.Empty
        } else {
            ComposeState.Chat(content = content, expend = false)
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("ComposeViewModel", "onCleared")
    }
}