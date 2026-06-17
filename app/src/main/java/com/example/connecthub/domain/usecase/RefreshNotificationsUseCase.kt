package com.example.connecthub.domain.usecase

import com.example.connecthub.domain.repository.NotificationRepository
import javax.inject.Inject

class RefreshNotificationsUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository,
) {
    suspend operator fun invoke() {
        notificationRepository.refreshNotifications()
    }
}
