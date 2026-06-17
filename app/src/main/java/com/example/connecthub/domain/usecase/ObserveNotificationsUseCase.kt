package com.example.connecthub.domain.usecase

import com.example.connecthub.domain.model.AppNotification
import com.example.connecthub.domain.repository.NotificationRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveNotificationsUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository,
) {
    operator fun invoke(): Flow<List<AppNotification>> = notificationRepository.observeNotifications()
}
