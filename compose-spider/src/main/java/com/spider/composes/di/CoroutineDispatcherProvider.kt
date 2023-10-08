package com.spider.composes.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

interface CoroutineDispatcherProvider {
    fun main(): CoroutineDispatcher = Dispatchers.Main
    fun default(): CoroutineDispatcher = Dispatchers.Default
    fun io(): CoroutineDispatcher = Dispatchers.IO
    fun unconfined(): CoroutineDispatcher = Dispatchers.Unconfined
}

object DefaultCoroutineDispatcherProvider : CoroutineDispatcherProvider

const val IO = "IO"
const val MAIN = "Main"

val dispatcherModule = module {
    single(qualifier = qualifier(IO)) { DefaultCoroutineDispatcherProvider.io() }
    single(qualifier = qualifier(MAIN)) { DefaultCoroutineDispatcherProvider.main() }
}
