package com.example.connecthub.data.notification

import com.example.connecthub.core.network.FakeNetworkConfig
import javax.inject.Inject
import kotlinx.coroutines.delay

class FakeNotificationApiService @Inject constructor() : NotificationApiService {
    override suspend fun getNotifications(): List<NotificationDto> {
        delay(FakeNetworkConfig.defaultDelayMillis)
        maybeThrowSimulatedError()
        return seedNotifications()
    }

    private fun maybeThrowSimulatedError() {
        if (FakeNetworkConfig.errorSimulationEnabled) {
            error("Fake notification service is simulating a network failure.")
        }
    }

    private fun seedNotifications(): List<NotificationDto> {
        val now = System.currentTimeMillis()
        return listOf(
            NotificationDto(
                id = "notification-like-1",
                type = "Like",
                title = "Maya liked your post",
                body = "Your architecture note is getting attention.",
                postId = "post-004",
                createdAt = now - 12 * 60_000,
            ),
            NotificationDto(
                id = "notification-comment-1",
                type = "Comment",
                title = "Omar commented on your update",
                body = "He replied to your note about Compose state.",
                postId = "post-004",
                createdAt = now - 60 * 60_000,
            ),
            NotificationDto(
                id = "notification-follow-1",
                type = "Follow",
                title = "Nadia started following you",
                body = "You have a new connection in your network.",
                postId = null,
                createdAt = now - 24 * 60 * 60_000,
            ),
            NotificationDto(
                id = "notification-mention-1",
                type = "Mention",
                title = "You were mentioned",
                body = "Maya mentioned you in a discussion about offline cache design.",
                postId = "post-001",
                createdAt = now - 2 * 24 * 60 * 60_000,
            ),
        )
    }
}
