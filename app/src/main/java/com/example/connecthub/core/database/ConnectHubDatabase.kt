package com.example.connecthub.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.connecthub.data.chat.ChatDao
import com.example.connecthub.data.chat.ConversationEntity
import com.example.connecthub.data.chat.MessageEntity
import com.example.connecthub.data.comment.CommentDao
import com.example.connecthub.data.comment.CommentEntity
import com.example.connecthub.data.feed.BookmarkedPostEntity
import com.example.connecthub.data.feed.PostDao
import com.example.connecthub.data.feed.PostEntity
import com.example.connecthub.data.notification.NotificationDao
import com.example.connecthub.data.notification.NotificationEntity
import com.example.connecthub.data.profile.ProfileDao
import com.example.connecthub.data.profile.ProfileEntity
import com.example.connecthub.data.search.RecentSearchDao
import com.example.connecthub.data.search.RecentSearchEntity

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
