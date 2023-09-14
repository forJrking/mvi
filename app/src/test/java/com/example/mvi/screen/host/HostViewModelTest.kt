package com.example.mvi.screen.host

import com.example.mvi.core.BaseTester
import com.example.mvi.core.effectCollection
import com.example.mvi.repo.Contact
import com.example.mvi.repo.DataRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.kotlin.wheneverBlocking

class HostViewModelTest : BaseTester() {

    private lateinit var viewModel: HostViewModel

    @Mock
    private lateinit var repository: DataRepository

    @BeforeEach
    override fun setUp() {
        super.setUp()
        val contacts = listOf(Contact(id = "01", content = "content", details = "details"))
        wheneverBlocking { repository.fetchData() }.thenReturn(contacts)
        viewModel = HostViewModel(repository)
    }

    @Test
    fun `test when LoadData action then return HostState_Data`() = runTest {
        viewModel.sendAction(HostAction.LoadData)
        val homeState = viewModel.state.first()
        assertInstanceOf(HostState.Data::class.java, homeState)
        (homeState as HostState.Data).data.first().run {
            assertEquals("01", id)
            assertEquals("content", content)
            assertEquals("details", details)
        }
    }

    @Test
    fun `test when NavCompose action then return HostState_Data`() = runTest {
        val element = Contact(id = "01", content = "content", details = "details")
        val homeEvent = viewModel.effectCollection(this)
        viewModel.sendAction(HostAction.NavCompose(element))
        assertEquals(HomeEvent.LaunchCompose(element), homeEvent.first())
    }
}
