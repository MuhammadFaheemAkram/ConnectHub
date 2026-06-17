package com.example.connecthub.domain.usecase

import com.example.connecthub.domain.repository.ChatRepository
import javax.inject.Inject

class RefreshMessagesUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
) {
    suspend operator fun invoke(conversationId: String) {
        chatRepository.refreshMessages(conversationId)
    }
}
