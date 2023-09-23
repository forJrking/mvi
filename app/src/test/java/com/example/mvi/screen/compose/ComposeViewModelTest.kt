package com.example.mvi.screen.compose

import android.net.Uri
import app.cash.turbine.test
import com.arch.mvi.testing.extension.effectCollection
import com.arch.mvi.testing.extension.stateCollection
import com.example.mvi.core.BaseTester
import com.example.mvi.repo.DataRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.whenever
import org.mockito.kotlin.wheneverBlocking

internal class ComposeViewModelTest : BaseTester() {

    private lateinit var viewModel: ComposeViewModel

    @Mock
    private lateinit var repository: DataRepository

    @BeforeEach
    override fun setUp() {
        super.setUp()
        viewModel = ComposeViewModel(repository, testDispatcherProvider)
    }

    @Test
    fun `test LoadData action then return wechat`() = runTest {
        wheneverBlocking { repository.fetchWeChat(any()) }.thenReturn(Result.success("response"))
        viewModel.sendAction(ComposeAction.LoadData("01"))
        //使用第三方库 https://github.com/cashapp/turbine
        viewModel.state.test {
            assertInstanceOf(ComposeState.Chat::class.java, awaitItem())
        }
    }

    @Test
    fun `test OnActionClicked action then return wechat`() = runTest {
        //使用扩展函数，通过集合实时获取最新的数据
        val effect = viewModel.effectCollection(this)
        viewModel.sendAction(ComposeAction.OnActionClicked)
        assertEquals(ComposeEvent.NavDetails, effect.first())
        viewModel.sendAction(ComposeAction.OnFABClicked)
        assertEquals(ComposeEvent.ShowWarring, effect[1])
    }

    @Test
    fun `test PickImages action then return ComposeState nullable`() = runTest {
        //使用扩展函数，通过集合实时获取最新的数据
        val states = viewModel.stateCollection(this)
        //加载数据
        viewModel.tryEmitState(ComposeState.Chat(content = "", expend = false))
        viewModel.sendAction(
            ComposeAction.PickImages(
                listOf(
                    mockUri("01"), mockUri("02"),
                    mockUri("03"), mockUri("04"),
                    mockUri("05"), mockUri("06")
                )
            )
        )
        (states.drop(1).first() as ComposeState.Chat).run {
            assertEquals(6, uri.size)
        }
        //累加2个
        viewModel.sendAction(ComposeAction.PickImages(listOf(mockUri("07"), mockUri("08"))))
        (states.drop(2).first() as ComposeState.Chat).run {
            assertEquals(6, uri.size)
        }
    }

    @Test
    fun `test WipeImage action then return ComposeState nullable`() = runTest {
        //使用扩展函数，通过集合实时获取最新的数据
        val states = viewModel.stateCollection(this)
        viewModel.sendAction(ComposeAction.WipeImage(mockUri("01")))
        assertEquals(null, states.firstOrNull())
        //加载数据
        viewModel.tryEmitState(
            ComposeState.Chat(
                uri = listOf(mockUri("01"), mockUri("02")),
                content = "",
                expend = false
            )
        )
        (viewModel.replayState as ComposeState.Chat).run {
            assertEquals(2, uri.size)
        }
        //删除图片
        viewModel.sendAction(ComposeAction.WipeImage(mockUri("01")))
    }

    private fun mockUri(id: String) = mock<Uri>().apply {
        whenever(toString()).doReturn(id)
    }
}