package com.example.connecthub.feature.notifications

import com.example.connecthub.core.testing.MainDispatcherRule
import com.example.connecthub.domain.model.NotificationType
import com.example.connecthub.domain.usecase.MarkAllNotificationsReadUseCase
import com.example.connecthub.domain.usecase.MarkNotificationReadUseCase
import com.example.connecthub.domain.usecase.ObserveNotificationsUseCase
import com.example.connecthub.domain.usecase.RefreshNotificationsUseCase
import com.example.connecthub.notification.FakeNotificationRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NotificationsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `loads notifications and supports category filter`() = runTest {
        val repository = FakeNotificationRepository(
            initialNotifications = listOf(
                FakeNotificationRepository.sampleNotification(id = "like", type = NotificationType.Like),
                FakeNotificationRepository.sampleNotification(id = "comment", type = NotificationType.Comment),
            ),
        )
        val viewModel = viewModel(repository)
        advanceUntilIdle()

        viewModel.selectFilter(NotificationFilter.Comment)

        assertTrue(repository.refreshed)
        assertEquals(listOf("comment"), viewModel.uiState.value.visibleNotifications.map { it.id })
    }

    @Test
    fun `mark all read updates unread count`() = runTest {
        val repository = FakeNotificationRepository()
        val viewModel = viewModel(repository)
        advanceUntilIdle()

        viewModel.markAllRead()
        advanceUntilIdle()

        assertTrue(repository.markedAllRead)
        assertEquals(0, viewModel.uiState.value.unreadCount)
    }

    private fun viewModel(repository: FakeNotificationRepository): NotificationsViewModel {
        return NotificationsViewModel(
            observeNotificationsUseCase = ObserveNotificationsUseCase(repository),
            refreshNotificationsUseCase = RefreshNotificationsUseCase(repository),
            markNotificationReadUseCase = MarkNotificationReadUseCase(repository),
            markAllNotificationsReadUseCase = MarkAllNotificationsReadUseCase(repository),
        )
    }
}
