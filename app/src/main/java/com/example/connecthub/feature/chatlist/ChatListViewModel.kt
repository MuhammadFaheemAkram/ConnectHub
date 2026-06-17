package com.example.connecthub.feature.chatlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.connecthub.domain.model.Conversation
import com.example.connecthub.domain.usecase.ObserveConversationsUseCase
import com.example.connecthub.domain.usecase.RefreshConversationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val observeConversationsUseCase: ObserveConversationsUseCase,
    private val refreshConversationsUseCase: RefreshConversationsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ChatListUiState())
    val uiState: StateFlow<ChatListUiState> = _uiState.asStateFlow()

    init {
        observeConversations()
        refresh()
    }

    fun onQueryChange(value: String) {
        _uiState.update { it.copy(query = value) }
    }

    fun refresh() {
        viewModelScope.launch {
            runCatching { refreshConversationsUseCase() }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = throwable.message ?: "Unable to refresh conversations",
                        )
                    }
                }
        }
    }

    private fun observeConversations() {
        viewModelScope.launch {
            observeConversationsUseCase()
                .catch { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = throwable.message ?: "Unable to load conversations",
                        )
                    }
                }
                .collect { conversations ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            conversations = conversations.map { conversation -> conversation.toUiModel() },
                            errorMessage = null,
                        )
                    }
                }
        }
    }
}

private fun Conversation.toUiModel(): ConversationUiModel = ConversationUiModel(
    id = id,
    participantName = participant.name,
    participantHeadline = participant.bio.ifBlank { "ConnectHub member" },
    lastMessage = lastMessage,
    unreadCount = unreadCount,
    updatedAt = updatedAt,
)
