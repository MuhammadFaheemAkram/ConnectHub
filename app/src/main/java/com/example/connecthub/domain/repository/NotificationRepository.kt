package com.example.connecthub.domain.repository

import com.example.connecthub.domain.model.AppNotification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun observeNotifications(): Flow<List<AppNotification>>
    suspend fun refreshNotifications()
    suspend fun markNotificationRead(notificationId: String)
    suspend fun markAllRead()
}
