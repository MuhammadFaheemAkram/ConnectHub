package co.bitfuse.connecthub.data.mapper

import co.bitfuse.connecthub.data.notification.NotificationDto
import co.bitfuse.connecthub.data.notification.NotificationEntity
import co.bitfuse.connecthub.domain.model.AppNotification
import co.bitfuse.connecthub.domain.model.NotificationType

fun NotificationDto.toEntity(
    isReadOverride: Boolean = false,
): NotificationEntity = NotificationEntity(
    id = id,
    type = type,
    title = title,
    body = body,
    postId = postId,
    createdAt = createdAt,
    isRead = isReadOverride,
)

fun NotificationEntity.toDomain(): AppNotification = AppNotification(
    id = id,
    type = type.toNotificationType(),
    title = title,
    body = body,
    postId = postId,
    createdAt = createdAt,
    isRead = isRead,
)

private fun String.toNotificationType(): NotificationType {
    return NotificationType.entries.firstOrNull { it.name.equals(this, ignoreCase = true) }
        ?: NotificationType.Mention
}
