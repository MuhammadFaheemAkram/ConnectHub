package co.bitfuse.connecthub.data.repository

import co.bitfuse.connecthub.core.common.dispatcher.DispatcherProvider
import co.bitfuse.connecthub.data.mapper.toDomain
import co.bitfuse.connecthub.data.mapper.toEntity
import co.bitfuse.connecthub.data.notification.NotificationApiService
import co.bitfuse.connecthub.data.notification.NotificationDao
import co.bitfuse.connecthub.domain.model.AppNotification
import co.bitfuse.connecthub.domain.repository.NotificationRepository
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
