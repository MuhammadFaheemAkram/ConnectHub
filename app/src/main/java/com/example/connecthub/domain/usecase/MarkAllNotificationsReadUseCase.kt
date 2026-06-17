package com.example.connecthub.domain.usecase

import com.example.connecthub.domain.repository.NotificationRepository
import javax.inject.Inject

class MarkAllNotificationsReadUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository,
) {
    suspend operator fun invoke() {
        notificationRepository.markAllRead()
    }
}
