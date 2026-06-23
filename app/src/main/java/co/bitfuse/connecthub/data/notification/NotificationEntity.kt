package co.bitfuse.connecthub.data.notification

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey val id: String,
    val type: String,
    val title: String,
    val body: String,
    val postId: String?,
    val createdAt: Long,
    val isRead: Boolean,
)
