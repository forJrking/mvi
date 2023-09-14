package com.example.mvi.repo

import com.example.mvi.di.DispatcherModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named
import kotlin.time.Duration.Companion.seconds

class DataRepositoryImpl @Inject constructor(
    @Named(DispatcherModule.IO) val ioDispatchers: CoroutineDispatcher
) : DataRepository {

    override suspend fun fetchData(): List<Contact> = withContext(ioDispatchers) { DataProviders.items }

    override suspend fun fetchWeChat(contactId: String) = withContext(ioDispatchers) {
        runCatching {
            delay(2.5.seconds)
            DataProviders.items.firstOrNull { it.id == contactId }?.details.let {
                """朋友圈一般指的是腾讯微信上的一个社交功能，$it"""
            }
        }
    }
}