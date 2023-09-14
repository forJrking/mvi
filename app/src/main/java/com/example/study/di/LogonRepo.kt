package com.example.study.di

import com.example.study.di.DispatcherModule.MAIN
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Named
import kotlin.random.Random

class LogonRepo @Inject constructor(
    @Named(MAIN) private val dispatcher: CoroutineDispatcher
) {

    fun getRandomNumber() = flow {
        delay(2500)
        emit(Random.nextInt(3))
    }.flowOn(dispatcher)
}