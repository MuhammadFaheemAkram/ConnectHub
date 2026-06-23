package co.bitfuse.connecthub.feature.notifications

import co.bitfuse.connecthub.domain.model.AppNotification
import co.bitfuse.connecthub.domain.model.NotificationType

enum class NotificationFilter {
    All,
    Unread,
    Like,
    Comment,
    Follow,
    Mention,
}

data class NotificationsUiState(
    val isLoading: Boolean = true,
    val notifications: List<AppNotification> = emptyList(),
    val selectedFilter: NotificationFilter = NotificationFilter.All,
    val errorMessage: String? = null,
) {
    val unreadCount: Int
        get() = notifications.count { !it.isRead }

    val visibleNotifications: List<AppNotification>
        get() = when (selectedFilter) {
            NotificationFilter.All -> notifications
            NotificationFilter.Unread -> notifications.filterNot { it.isRead }
            NotificationFilter.Like -> notifications.filter { it.type == NotificationType.Like }
            NotificationFilter.Comment -> notifications.filter { it.type == NotificationType.Comment }
            NotificationFilter.Follow -> notifications.filter { it.type == NotificationType.Follow }
            NotificationFilter.Mention -> notifications.filter { it.type == NotificationType.Mention }
        }
}
