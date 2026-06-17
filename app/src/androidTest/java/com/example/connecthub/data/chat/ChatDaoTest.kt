package com.example.connecthub.data.chat

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
class ChatDaoTest {
    private lateinit var database: ConnectHubDatabase
    private lateinit var chatDao: ChatDao

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(
            context,
            ConnectHubDatabase::class.java,
        ).allowMainThreadQueries().build()
        chatDao = database.chatDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertReadConversationAndMessages() = runTest {
        chatDao.upsertConversations(listOf(testConversation()))
        chatDao.upsertMessages(
            listOf(
                testMessage(id = "message-1"),
                testMessage(id = "message-2", text = "Reply"),
            ),
        )

        assertEquals(listOf("conversation-1"), chatDao.observeConversations().first().map { it.id })
        assertEquals(listOf("message-1", "message-2"), chatDao.observeMessages("conversation-1").first().map { it.id })
    }

    @Test
    fun updateConversationPreview() = runTest {
        chatDao.upsertConversations(listOf(testConversation()))

        chatDao.updateConversationPreview(
            conversationId = "conversation-1",
            lastMessage = "Updated",
            updatedAt = 2_000L,
            unreadCount = 0,
        )

        val conversation = chatDao.getConversation("conversation-1")
        assertEquals("Updated", conversation?.lastMessage)
        assertEquals(0, conversation?.unreadCount)
    }

    private fun testConversation(): ConversationEntity = ConversationEntity(
        id = "conversation-1",
        participantId = "user-maya",
        participantName = "Maya Chen",
        participantAvatarUrl = "",
        participantBio = "Android Engineer",
        participantFollowersCount = 10,
        participantFollowingCount = 4,
        lastMessage = "Hello",
        unreadCount = 1,
        updatedAt = 1_000L,
    )

    private fun testMessage(
        id: String,
        text: String = "Hello",
    ): MessageEntity = MessageEntity(
        id = id,
        conversationId = "conversation-1",
        senderId = "user-maya",
        text = text,
        createdAt = 1_000L,
        isMine = false,
    )
}
