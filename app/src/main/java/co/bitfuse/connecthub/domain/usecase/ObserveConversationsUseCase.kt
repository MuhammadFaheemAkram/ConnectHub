package co.bitfuse.connecthub.domain.usecase

import co.bitfuse.connecthub.domain.model.Conversation
import co.bitfuse.connecthub.domain.repository.ChatRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveConversationsUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
) {
    operator fun invoke(): Flow<List<Conversation>> = chatRepository.observeConversations()
}
