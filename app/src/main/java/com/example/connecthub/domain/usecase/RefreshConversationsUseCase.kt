package com.example.connecthub.domain.usecase

import com.example.connecthub.domain.repository.ChatRepository
import javax.inject.Inject

class RefreshConversationsUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
) {
    suspend operator fun invoke() {
        chatRepository.refreshConversations()
    }
}
