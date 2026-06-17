package com.example.connecthub.data.repository

import com.example.connecthub.core.common.dispatcher.DispatcherProvider
import com.example.connecthub.data.mapper.toDomain
import com.example.connecthub.data.mapper.toEntity
import com.example.connecthub.data.notification.NotificationApiService
import com.example.connecthub.data.notification.NotificationDao
import com.example.connecthub.domain.model.AppNotification
import com.example.connecthub.domain.repository.NotificationRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class NotificationRepositoryImpl @Inject constructor(
    private val notificationApiService: NotificationApiService,
    private val notificationDao: NotificationDao,
    private val dispatchers: DispatcherProvider,
) : NotificationRepository {
    override fun observeNotifications(): Flow<List<AppNotification>> {
        return notificationDao.observeNotifications().map { notifications ->
            notifications.map { it.toDomain() }
        }
    }

    override suspend fun refreshNotifications() {
        withContext(dispatchers.io) {
            val readIds = notificationDao.getReadNotificationIds().toSet()
            notificationDao.upsertNotifications(
                notificationApiService.getNotifications().map { notification ->
                    notification.toEntity(isReadOverride = notification.id in readIds)
                },
            )
        }
    }

    override suspend fun markNotificationRead(notificationId: String) {
        withContext(dispatchers.io) {
            notificationDao.markNotificationRead(notificationId)
        }
    }

    override suspend fun markAllRead() {
        withContext(dispatchers.io) {
            notificationDao.markAllRead()
        }
    }
}
