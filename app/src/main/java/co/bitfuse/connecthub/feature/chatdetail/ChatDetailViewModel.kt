package co.bitfuse.connecthub.feature.chatdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.bitfuse.connecthub.domain.model.Message
import co.bitfuse.connecthub.domain.usecase.ObserveMessagesUseCase
import co.bitfuse.connecthub.domain.usecase.RefreshMessagesUseCase
import co.bitfuse.connecthub.domain.usecase.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ChatDetailViewModel @Inject constructor(
    private val observeMessagesUseCase: ObserveMessagesUseCase,
    private val refreshMessagesUseCase: RefreshMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ChatDetailUiState())
    val uiState: StateFlow<ChatDetailUiState> = _uiState.asStateFlow()

    private var conversationId: String? = null
    private var observeJob: Job? = null

    fun load(conversationId: String) {
        if (this.conversationId == conversationId) return
        this.conversationId = conversationId
        observeJob?.cancel()
        observeMessages(conversationId)
        refresh(conversationId)
    }

    fun onDraftChange(value: String) {
        _uiState.update {
            it.copy(
                draft = value,
                errorMessage = null,
            )
        }
    }

    fun send() {
        val targetConversationId = conversationId ?: return
        val text = uiState.value.draft.trim()
        if (text.isBlank()) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    draft = "",
                    isSending = true,
                    isTyping = true,
                    errorMessage = null,
                )
            }
            runCatching { sendMessageUseCase(targetConversationId, text) }
                .onSuccess {
                    _uiState.update { it.copy(isSending = false, isTyping = false) }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isSending = false,
                            isTyping = false,
                            errorMessage = throwable.message ?: "Unable to send message",
                        )
                    }
                }
        }
    }

    private fun observeMessages(conversationId: String) {
        observeJob = viewModelScope.launch {
            observeMessagesUseCase(conversationId)
                .catch { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = throwable.message ?: "Unable to load messages",
                        )
                    }
                }
                .collect { messages ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            messages = messages.map { message -> message.toUiModel() },
                            errorMessage = null,
                        )
                    }
                }
        }
    }

    private fun refresh(conversationId: String) {
        viewModelScope.launch {
            runCatching { refreshMessagesUseCase(conversationId) }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = throwable.message ?: "Unable to refresh messages",
                        )
                    }
                }
        }
    }
}

private fun Message.toUiModel(): MessageUiModel = MessageUiModel(
    id = id,
    text = text,
    isMine = isMine,
)
