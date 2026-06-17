package com.example.connecthub.feature.chatdetail

import com.example.connecthub.chat.FakeChatRepository
import com.example.connecthub.core.testing.MainDispatcherRule
import com.example.connecthub.domain.usecase.ObserveMessagesUseCase
import com.example.connecthub.domain.usecase.RefreshMessagesUseCase
import com.example.connecthub.domain.usecase.SendMessageUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChatDetailViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `load observes messages and refreshes conversation`() = runTest {
        val repository = FakeChatRepository()
        val viewModel = viewModel(repository)

        viewModel.load("conversation-1")
        advanceUntilIdle()

        assertEquals("conversation-1", repository.refreshedMessagesFor)
        assertEquals(listOf("message-1"), viewModel.uiState.value.messages.map { it.id })
    }

    @Test
    fun `send message clears draft and receives simulated reply`() = runTest {
        val repository = FakeChatRepository()
        val viewModel = viewModel(repository)
        viewModel.load("conversation-1")
        advanceUntilIdle()

        viewModel.onDraftChange("  hello  ")
        viewModel.send()
        advanceUntilIdle()

        assertEquals("hello", repository.sentText)
        assertEquals("", viewModel.uiState.value.draft)
        assertFalse(viewModel.uiState.value.isTyping)
        assertEquals(listOf("message-1", "sent-message", "reply-message"), viewModel.uiState.value.messages.map { it.id })
    }

    private fun viewModel(repository: FakeChatRepository): ChatDetailViewModel {
        return ChatDetailViewModel(
            observeMessagesUseCase = ObserveMessagesUseCase(repository),
            refreshMessagesUseCase = RefreshMessagesUseCase(repository),
            sendMessageUseCase = SendMessageUseCase(repository),
        )
    }
}
