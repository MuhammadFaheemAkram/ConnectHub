package co.bitfuse.connecthub.domain.usecase

import co.bitfuse.connecthub.domain.repository.ChatRepository
import javax.inject.Inject

class RefreshConversationsUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
) {
    suspend operator fun invoke() {
        chatRepository.refreshConversations()
    }
}
