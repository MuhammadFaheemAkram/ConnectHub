package co.bitfuse.connecthub.domain.usecase

import co.bitfuse.connecthub.domain.repository.ChatRepository
import javax.inject.Inject

class RefreshMessagesUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
) {
    suspend operator fun invoke(conversationId: String) {
        chatRepository.refreshMessages(conversationId)
    }
}
