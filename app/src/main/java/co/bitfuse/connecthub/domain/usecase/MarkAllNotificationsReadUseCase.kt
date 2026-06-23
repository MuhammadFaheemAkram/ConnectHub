package co.bitfuse.connecthub.domain.usecase

import co.bitfuse.connecthub.domain.repository.NotificationRepository
import javax.inject.Inject

class MarkAllNotificationsReadUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository,
) {
    suspend operator fun invoke() {
        notificationRepository.markAllRead()
    }
}
