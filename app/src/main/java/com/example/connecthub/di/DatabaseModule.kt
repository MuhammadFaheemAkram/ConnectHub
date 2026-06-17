package com.example.connecthub.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.connecthub.core.database.ConnectHubDatabase
import com.example.connecthub.core.database.DatabasePlaceholder
import com.example.connecthub.data.chat.ChatDao
import com.example.connecthub.data.comment.CommentDao
import com.example.connecthub.data.feed.PostDao
import com.example.connecthub.data.notification.NotificationDao
import com.example.connecthub.data.profile.ProfileDao
import com.example.connecthub.data.search.RecentSearchDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideConnectHubDatabase(
        @ApplicationContext context: Context,
    ): ConnectHubDatabase {
        return Room.databaseBuilder(
            context,
            ConnectHubDatabase::class.java,
            DatabasePlaceholder.databaseName,
        ).addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4).build()
    }

    @Provides
    fun providePostDao(database: ConnectHubDatabase): PostDao = database.postDao()

    @Provides
    fun provideCommentDao(database: ConnectHubDatabase): CommentDao = database.commentDao()

    @Provides
    fun provideRecentSearchDao(database: ConnectHubDatabase): RecentSearchDao = database.recentSearchDao()

    @Provides
    fun provideProfileDao(database: ConnectHubDatabase): ProfileDao = database.profileDao()

    @Provides
    fun provideChatDao(database: ConnectHubDatabase): ChatDao = database.chatDao()

    @Provides
    fun provideNotificationDao(database: ConnectHubDatabase): NotificationDao = database.notificationDao()

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `comments` (
                    `id` TEXT NOT NULL,
                    `postId` TEXT NOT NULL,
                    `authorId` TEXT NOT NULL,
                    `authorName` TEXT NOT NULL,
                    `authorAvatarUrl` TEXT NOT NULL,
                    `authorBio` TEXT NOT NULL,
                    `authorFollowersCount` INTEGER NOT NULL,
                    `authorFollowingCount` INTEGER NOT NULL,
                    `text` TEXT NOT NULL,
                    `createdAt` INTEGER NOT NULL,
                    `isOwnComment` INTEGER NOT NULL,
                    PRIMARY KEY(`id`),
                    FOREIGN KEY(`postId`) REFERENCES `posts`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
                )
                """.trimIndent(),
            )
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_comments_postId` ON `comments` (`postId`)")
        }
    }

    private val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `recent_searches` (
                    `query` TEXT NOT NULL,
                    `searchedAt` INTEGER NOT NULL,
                    PRIMARY KEY(`query`)
                )
                """.trimIndent(),
            )
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `profiles` (
                    `id` TEXT NOT NULL,
                    `name` TEXT NOT NULL,
                    `avatarUrl` TEXT NOT NULL,
                    `bio` TEXT NOT NULL,
                    `followersCount` INTEGER NOT NULL,
                    `followingCount` INTEGER NOT NULL,
                    PRIMARY KEY(`id`)
                )
                """.trimIndent(),
            )
            db.execSQL(
                """
                INSERT OR IGNORE INTO `profiles`
                (`id`, `name`, `avatarUrl`, `bio`, `followersCount`, `followingCount`)
                VALUES (
                    'user-alex',
                    'Alex Morgan',
                    'https://images.unsplash.com/photo-1517841905240-472988babdf9',
                    'Building thoughtful Android apps with Compose.',
                    1240,
                    318
                )
                """.trimIndent(),
            )
        }
    }

    private val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `conversations` (
                    `id` TEXT NOT NULL,
                    `participantId` TEXT NOT NULL,
                    `participantName` TEXT NOT NULL,
                    `participantAvatarUrl` TEXT NOT NULL,
                    `participantBio` TEXT NOT NULL,
                    `participantFollowersCount` INTEGER NOT NULL,
                    `participantFollowingCount` INTEGER NOT NULL,
                    `lastMessage` TEXT NOT NULL,
                    `unreadCount` INTEGER NOT NULL,
                    `updatedAt` INTEGER NOT NULL,
                    PRIMARY KEY(`id`)
                )
                """.trimIndent(),
            )
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `messages` (
                    `id` TEXT NOT NULL,
                    `conversationId` TEXT NOT NULL,
                    `senderId` TEXT NOT NULL,
                    `text` TEXT NOT NULL,
                    `createdAt` INTEGER NOT NULL,
                    `isMine` INTEGER NOT NULL,
                    PRIMARY KEY(`id`),
                    FOREIGN KEY(`conversationId`) REFERENCES `conversations`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
                )
                """.trimIndent(),
            )
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_messages_conversationId` ON `messages` (`conversationId`)")
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `notifications` (
                    `id` TEXT NOT NULL,
                    `type` TEXT NOT NULL,
                    `title` TEXT NOT NULL,
                    `body` TEXT NOT NULL,
                    `postId` TEXT,
                    `createdAt` INTEGER NOT NULL,
                    `isRead` INTEGER NOT NULL,
                    PRIMARY KEY(`id`)
                )
                """.trimIndent(),
            )
        }
    }
}
