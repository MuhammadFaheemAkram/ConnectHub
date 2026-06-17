package com.example.connecthub.domain.usecase

import com.example.connecthub.domain.repository.ChatRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
) {
    suspend operator fun invoke(conversationId: String, text: String) {
        chatRepository.sendMessage(conversationId = conversationId, text = text.trim())
    }
}
