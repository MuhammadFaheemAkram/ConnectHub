package com.example.connecthub.feature.chatdetail

data class MessageUiModel(
    val id: String,
    val text: String,
    val isMine: Boolean,
)

data class ChatDetailUiState(
    val isLoading: Boolean = true,
    val messages: List<MessageUiModel> = emptyList(),
    val draft: String = "",
    val isSending: Boolean = false,
    val isTyping: Boolean = false,
    val errorMessage: String? = null,
) {
    val canSend: Boolean
        get() = draft.isNotBlank() && !isSending
}
