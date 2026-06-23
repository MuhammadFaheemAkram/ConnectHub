package co.bitfuse.connecthub.domain.usecase

import co.bitfuse.connecthub.domain.repository.ChatRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
) {
    suspend operator fun invoke(conversationId: String, text: String) {
        chatRepository.sendMessage(conversationId = conversationId, text = text.trim())
    }
}
