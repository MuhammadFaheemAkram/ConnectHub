package com.example.connecthub.feature.chatlist

import com.example.connecthub.chat.FakeChatRepository
import com.example.connecthub.core.testing.MainDispatcherRule
import com.example.connecthub.domain.usecase.ObserveConversationsUseCase
import com.example.connecthub.domain.usecase.RefreshConversationsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChatListViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `loads conversations and refreshes on init`() = runTest {
        val repository = FakeChatRepository()
        val viewModel = ChatListViewModel(
            observeConversationsUseCase = ObserveConversationsUseCase(repository),
            refreshConversationsUseCase = RefreshConversationsUseCase(repository),
        )

        advanceUntilIdle()

        assertTrue(repository.refreshedConversations)
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(listOf("conversation-1"), viewModel.uiState.value.conversations.map { it.id })
    }

    @Test
    fun `query filters visible conversations`() = runTest {
        val viewModel = ChatListViewModel(
            observeConversationsUseCase = ObserveConversationsUseCase(FakeChatRepository()),
            refreshConversationsUseCase = RefreshConversationsUseCase(FakeChatRepository()),
        )
        advanceUntilIdle()

        viewModel.onQueryChange("maya")

        assertEquals(listOf("conversation-1"), viewModel.uiState.value.visibleConversations.map { it.id })
    }
}
