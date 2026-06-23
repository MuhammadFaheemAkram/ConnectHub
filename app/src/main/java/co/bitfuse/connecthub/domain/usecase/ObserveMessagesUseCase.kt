package co.bitfuse.connecthub.domain.usecase

import co.bitfuse.connecthub.domain.model.Message
import co.bitfuse.connecthub.domain.repository.ChatRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveMessagesUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
) {
    operator fun invoke(conversationId: String): Flow<List<Message>> = chatRepository.observeMessages(conversationId)
}
