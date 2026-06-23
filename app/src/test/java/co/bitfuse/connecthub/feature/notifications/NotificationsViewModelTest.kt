package co.bitfuse.connecthub.feature.notifications

import co.bitfuse.connecthub.core.testing.MainDispatcherRule
import co.bitfuse.connecthub.domain.model.NotificationType
import co.bitfuse.connecthub.domain.usecase.MarkAllNotificationsReadUseCase
import co.bitfuse.connecthub.domain.usecase.MarkNotificationReadUseCase
import co.bitfuse.connecthub.domain.usecase.ObserveNotificationsUseCase
import co.bitfuse.connecthub.domain.usecase.RefreshNotificationsUseCase
import co.bitfuse.connecthub.notification.FakeNotificationRepository
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
