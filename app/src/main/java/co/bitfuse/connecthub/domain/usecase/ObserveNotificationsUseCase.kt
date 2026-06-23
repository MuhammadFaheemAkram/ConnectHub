package co.bitfuse.connecthub.domain.usecase

import co.bitfuse.connecthub.domain.model.AppNotification
import co.bitfuse.connecthub.domain.repository.NotificationRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveNotificationsUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository,
) {
    operator fun invoke(): Flow<List<AppNotification>> = notificationRepository.observeNotifications()
}
