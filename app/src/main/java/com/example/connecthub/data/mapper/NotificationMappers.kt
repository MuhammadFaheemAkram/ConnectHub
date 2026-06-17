package com.example.connecthub.data.mapper

import com.example.connecthub.data.notification.NotificationDto
import com.example.connecthub.data.notification.NotificationEntity
import com.example.connecthub.domain.model.AppNotification
import com.example.connecthub.domain.model.NotificationType

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
