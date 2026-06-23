package co.bitfuse.connecthub.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import co.bitfuse.connecthub.data.chat.ChatDao
import co.bitfuse.connecthub.data.chat.ConversationEntity
import co.bitfuse.connecthub.data.chat.MessageEntity
import co.bitfuse.connecthub.data.comment.CommentDao
import co.bitfuse.connecthub.data.comment.CommentEntity
import co.bitfuse.connecthub.data.feed.BookmarkedPostEntity
import co.bitfuse.connecthub.data.feed.PostDao
import co.bitfuse.connecthub.data.feed.PostEntity
import co.bitfuse.connecthub.data.notification.NotificationDao
import co.bitfuse.connecthub.data.notification.NotificationEntity
import co.bitfuse.connecthub.data.profile.ProfileDao
import co.bitfuse.connecthub.data.profile.ProfileEntity
import co.bitfuse.connecthub.data.search.RecentSearchDao
import co.bitfuse.connecthub.data.search.RecentSearchEntity

@Database(
    entities = [
        PostEntity::class,
        BookmarkedPostEntity::class,
        CommentEntity::class,
        RecentSearchEntity::class,
        ProfileEntity::class,
        ConversationEntity::class,
        MessageEntity::class,
        NotificationEntity::class,
    ],
    version = 4,
    exportSchema = false,
)
abstract class ConnectHubDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun commentDao(): CommentDao
    abstract fun recentSearchDao(): RecentSearchDao
    abstract fun profileDao(): ProfileDao
    abstract fun chatDao(): ChatDao
    abstract fun notificationDao(): NotificationDao
}
