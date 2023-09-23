@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.mvi.core

import com.arch.mvi.testing.extension.CoroutineTestExtension
import com.arch.mvi.testing.extension.InstantTaskExecutorExtension
import com.example.mvi.di.CoroutineDispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.MockitoAnnotations


@ExtendWith(
    value = [CoroutineTestExtension::class, InstantTaskExecutorExtension::class]
)
open class BaseTester {

    protected val testDispatcher: TestDispatcher
        get() = CoroutineTestExtension.testDispatcher

    protected val testDispatcherProvider: CoroutineDispatcherProvider = TestCoroutineDispatcherProvider(testDispatcher)

    private lateinit var openMocks: AutoCloseable

    @BeforeEach
    open fun setUp() {
        openMocks = MockitoAnnotations.openMocks(this)
    }

    @AfterEach
    open fun tearDown() {
        openMocks.close()
    }
}

class TestCoroutineDispatcherProvider(
    private val testDispatcher: TestDispatcher
) : CoroutineDispatcherProvider {
    override fun main(): CoroutineDispatcher = testDispatcher
    override fun default(): CoroutineDispatcher = testDispatcher
    override fun io(): CoroutineDispatcher = testDispatcher
    override fun unconfined(): CoroutineDispatcher = testDispatcher
}
