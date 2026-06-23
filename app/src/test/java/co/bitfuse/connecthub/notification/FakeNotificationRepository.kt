package co.bitfuse.connecthub.notification

import co.bitfuse.connecthub.domain.model.AppNotification
import co.bitfuse.connecthub.domain.model.NotificationType
import co.bitfuse.connecthub.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeNotificationRepository(
    initialNotifications: List<AppNotification> = listOf(sampleNotification()),
) : NotificationRepository {
    private val notifications = MutableStateFlow(initialNotifications)
    var refreshed = false
    var markedReadId: String? = null
    var markedAllRead = false

    override fun observeNotifications(): Flow<List<AppNotification>> = notifications

    override suspend fun refreshNotifications() {
        refreshed = true
    }

    override suspend fun markNotificationRead(notificationId: String) {
        markedReadId = notificationId
        notifications.value = notifications.value.map {
            if (it.id == notificationId) it.copy(isRead = true) else it
        }
    }

    override suspend fun markAllRead() {
        markedAllRead = true
        notifications.value = notifications.value.map { it.copy(isRead = true) }
    }

    companion object {
        fun sampleNotification(
            id: String = "notification-1",
            type: NotificationType = NotificationType.Like,
            isRead: Boolean = false,
        ): AppNotification = AppNotification(
            id = id,
            type = type,
            title = "Maya liked your post",
            body = "A test notification",
            postId = "post-1",
            createdAt = 1_000L,
            isRead = isRead,
        )
    }
}
