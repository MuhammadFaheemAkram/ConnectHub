package co.bitfuse.connecthub.feature.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.bitfuse.connecthub.domain.usecase.MarkAllNotificationsReadUseCase
import co.bitfuse.connecthub.domain.usecase.MarkNotificationReadUseCase
import co.bitfuse.connecthub.domain.usecase.ObserveNotificationsUseCase
import co.bitfuse.connecthub.domain.usecase.RefreshNotificationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val observeNotificationsUseCase: ObserveNotificationsUseCase,
    private val refreshNotificationsUseCase: RefreshNotificationsUseCase,
    private val markNotificationReadUseCase: MarkNotificationReadUseCase,
    private val markAllNotificationsReadUseCase: MarkAllNotificationsReadUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()

    init {
        observeNotifications()
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            runCatching { refreshNotificationsUseCase() }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = throwable.message ?: "Unable to refresh notifications",
                        )
                    }
                }
        }
    }

    fun selectFilter(filter: NotificationFilter) {
        _uiState.update { it.copy(selectedFilter = filter) }
    }

    fun markRead(notificationId: String) {
        viewModelScope.launch {
            runCatching { markNotificationReadUseCase(notificationId) }
                .onFailure { throwable ->
                    _uiState.update { it.copy(errorMessage = throwable.message ?: "Unable to mark notification read") }
                }
        }
    }

    fun markAllRead() {
        viewModelScope.launch {
            runCatching { markAllNotificationsReadUseCase() }
                .onFailure { throwable ->
                    _uiState.update { it.copy(errorMessage = throwable.message ?: "Unable to mark all notifications read") }
                }
        }
    }

    private fun observeNotifications() {
        viewModelScope.launch {
            observeNotificationsUseCase()
                .catch { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = throwable.message ?: "Unable to load notifications",
                        )
                    }
                }
                .collect { notifications ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            notifications = notifications,
                            errorMessage = null,
                        )
                    }
                }
        }
    }
}
