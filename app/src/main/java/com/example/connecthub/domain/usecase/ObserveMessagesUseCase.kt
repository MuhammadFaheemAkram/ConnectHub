package com.example.connecthub.domain.usecase

import com.example.connecthub.domain.model.Message
import com.example.connecthub.domain.repository.ChatRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveMessagesUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
) {
    operator fun invoke(conversationId: String): Flow<List<Message>> = chatRepository.observeMessages(conversationId)
}
