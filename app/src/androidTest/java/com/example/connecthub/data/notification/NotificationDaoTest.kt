package com.example.connecthub.data.notification

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.connecthub.core.database.ConnectHubDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NotificationDaoTest {
    private lateinit var database: ConnectHubDatabase
    private lateinit var notificationDao: NotificationDao

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(
            context,
            ConnectHubDatabase::class.java,
        ).allowMainThreadQueries().build()
        notificationDao = database.notificationDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun markNotificationReadAndMarkAllRead() = runTest {
        notificationDao.upsertNotifications(
            listOf(
                testNotification(id = "notification-1"),
                testNotification(id = "notification-2"),
            ),
        )

        notificationDao.markNotificationRead("notification-1")

        assertEquals(listOf("notification-1"), notificationDao.getReadNotificationIds())

        notificationDao.markAllRead()

        assertEquals(
            listOf("notification-1", "notification-2"),
            notificationDao.observeNotifications().first().filter { it.isRead }.map { it.id }.sorted(),
        )
    }

    private fun testNotification(id: String): NotificationEntity = NotificationEntity(
        id = id,
        type = "Like",
        title = "Maya liked your post",
        body = "A test notification",
        postId = "post-1",
        createdAt = 1_000L,
        isRead = false,
    )
}
