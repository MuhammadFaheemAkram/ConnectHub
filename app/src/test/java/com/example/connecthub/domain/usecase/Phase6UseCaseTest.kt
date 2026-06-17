package com.example.connecthub.domain.usecase

import com.example.connecthub.chat.FakeChatRepository
import com.example.connecthub.notification.FakeNotificationRepository
import com.example.connecthub.settings.FakeSettingsRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class Phase6UseCaseTest {
    @Test
    fun `SendMessageUseCase trims text before delegating`() = runTest {
        val repository = FakeChatRepository()

        SendMessageUseCase(repository)("conversation-1", "  hello  ")

        assertEquals("hello", repository.sentText)
    }

    @Test
    fun `MarkNotificationReadUseCase delegates notification id`() = runTest {
        val repository = FakeNotificationRepository()

        MarkNotificationReadUseCase(repository)("notification-1")

        assertEquals("notification-1", repository.markedReadId)
    }

    @Test
    fun `UpdateNotificationSettingUseCase persists enabled state`() = runTest {
        val repository = FakeSettingsRepository()

        UpdateNotificationSettingUseCase(repository)(false)

        assertFalse(repository.observeSettings().first().notificationsEnabled)
    }

    @Test
    fun `ClearCacheUseCase delegates cache clear`() = runTest {
        val repository = FakeSettingsRepository()

        ClearCacheUseCase(repository)()

        assertTrue(repository.cacheCleared)
    }
}
