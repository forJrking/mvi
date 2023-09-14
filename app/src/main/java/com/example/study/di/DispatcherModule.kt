package com.example.study.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {

    const val IO = "IO"
    const val MAIN = "Main"

    @Provides
    fun provideCoroutineDispatcherProvider(): CoroutineDispatcherProvider =
        DefaultCoroutineDispatcherProvider()

    @Named(IO)
    @Provides
    fun provideIODispatchers(
        provider: CoroutineDispatcherProvider
    ): CoroutineDispatcher = provider.io()

    @Named(MAIN)
    @Provides
    fun provideMainDispatchers(
        provider: CoroutineDispatcherProvider
    ): CoroutineDispatcher = provider.main()
}