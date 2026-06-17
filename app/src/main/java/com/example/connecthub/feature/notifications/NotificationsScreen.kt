package com.example.connecthub.feature.notifications

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AlternateEmail
import androidx.compose.material.icons.outlined.DoneAll
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.ModeComment
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material3.AssistChip
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.connecthub.core.designsystem.component.ConnectHubEmptyState
import com.example.connecthub.core.designsystem.component.ConnectHubErrorState
import com.example.connecthub.core.designsystem.component.ConnectHubLoadingState
import com.example.connecthub.domain.model.AppNotification
import com.example.connecthub.domain.model.NotificationType

@Composable
fun NotificationsRoute(
    onOpenPost: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NotificationsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NotificationsScreen(
        uiState = uiState,
        onSelectFilter = viewModel::selectFilter,
        onMarkRead = viewModel::markRead,
        onMarkAllRead = viewModel::markAllRead,
        onRetry = viewModel::refresh,
        onOpenPost = onOpenPost,
        modifier = modifier,
    )
}

@Composable
fun NotificationsScreen(
    uiState: NotificationsUiState,
    onSelectFilter: (NotificationFilter) -> Unit,
    onMarkRead: (String) -> Unit,
    onMarkAllRead: () -> Unit,
    onRetry: () -> Unit,
    onOpenPost: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    when {
        uiState.isLoading -> ConnectHubLoadingState(
            message = "Loading notifications",
            modifier = modifier.fillMaxSize(),
        )

        uiState.errorMessage != null && uiState.notifications.isEmpty() -> ConnectHubErrorState(
            title = "Notifications unavailable",
            message = uiState.errorMessage,
            onRetry = onRetry,
            modifier = modifier.fillMaxSize(),
        )

        else -> LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(NotificationFilter.entries) { filter ->
                        FilterChip(
                            selected = uiState.selectedFilter == filter,
                            onClick = { onSelectFilter(filter) },
                            label = { Text(text = filter.label) },
                        )
                    }
                }
            }

            item {
                AssistChip(
                    onClick = onMarkAllRead,
                    enabled = uiState.unreadCount > 0,
                    label = { Text(text = "Mark all read (${uiState.unreadCount})") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.DoneAll,
                            contentDescription = null,
                        )
                    },
                )
            }

            uiState.errorMessage?.let { message ->
                item {
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }

            if (uiState.visibleNotifications.isEmpty()) {
                item {
                    ConnectHubEmptyState(
                        title = "No notifications",
                        message = "There is nothing in this category right now.",
                        icon = Icons.Outlined.NotificationsNone,
                    )
                }
            } else {
                items(
                    items = uiState.visibleNotifications,
                    key = { it.id },
                ) { notification ->
                    NotificationRow(
                        notification = notification,
                        onMarkRead = onMarkRead,
                        onOpenPost = onOpenPost,
                    )
                }
            }
        }
    }
}

@Composable
private fun NotificationRow(
    notification: AppNotification,
    onMarkRead: (String) -> Unit,
    onOpenPost: (String) -> Unit,
) {
    val clickModifier = if (notification.postId != null) {
        Modifier.clickable {
            onMarkRead(notification.id)
            onOpenPost(notification.postId)
        }
    } else {
        Modifier.clickable { onMarkRead(notification.id) }
    }

    ListItem(
        headlineContent = {
            Text(
                text = notification.title,
                fontWeight = if (notification.isRead) FontWeight.Normal else FontWeight.SemiBold,
            )
        },
        supportingContent = { Text(notification.body) },
        leadingContent = {
            Icon(
                imageVector = notification.type.icon,
                contentDescription = null,
                tint = if (notification.isRead) {
                    MaterialTheme.colorScheme.onSurfaceVariant
                } else {
                    MaterialTheme.colorScheme.primary
                },
            )
        },
        trailingContent = {
            if (!notification.isRead) {
                IconButton(onClick = { onMarkRead(notification.id) }) {
                    Icon(
                        imageVector = Icons.Outlined.DoneAll,
                        contentDescription = "Mark notification read",
                    )
                }
            }
        },
        modifier = clickModifier,
    )
}

private val NotificationFilter.label: String
    get() = when (this) {
        NotificationFilter.All -> "All"
        NotificationFilter.Unread -> "Unread"
        NotificationFilter.Like -> "Likes"
        NotificationFilter.Comment -> "Comments"
        NotificationFilter.Follow -> "Follows"
        NotificationFilter.Mention -> "Mentions"
    }

private val NotificationType.icon: ImageVector
    get() = when (this) {
        NotificationType.Like -> Icons.Outlined.FavoriteBorder
        NotificationType.Comment -> Icons.Outlined.ModeComment
        NotificationType.Follow -> Icons.Outlined.PersonAdd
        NotificationType.Mention -> Icons.Outlined.AlternateEmail
    }
