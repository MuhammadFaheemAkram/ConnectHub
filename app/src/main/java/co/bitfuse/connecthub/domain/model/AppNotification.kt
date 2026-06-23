package co.bitfuse.connecthub.domain.model

data class AppNotification(
    val id: String,
    val type: NotificationType,
    val title: String,
    val body: String,
    val postId: String?,
    val createdAt: Long,
    val isRead: Boolean,
)

enum class NotificationType {
    Like,
    Comment,
    Follow,
    Mention,
}
