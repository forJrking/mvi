package com.arch.mvi.testing

import com.arch.mvi.testing.extension.CoroutineTestExtension
import com.arch.mvi.testing.extension.InstantTaskExecutorExtension
import kotlinx.coroutines.test.TestDispatcher
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(
    value = [
        MockitoExtension::class,
        CoroutineTestExtension::class,
        InstantTaskExecutorExtension::class
    ]
)
abstract class BaseTester {

    protected val testDispatcher: TestDispatcher
        get() = CoroutineTestExtension.testDispatcher

    @BeforeEach
    abstract fun setUp()

    @AfterEach
    open fun tearDown() {
    }
}
