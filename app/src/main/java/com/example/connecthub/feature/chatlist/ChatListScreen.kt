package com.example.connecthub.feature.chatlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.connecthub.core.designsystem.component.ConnectHubAvatar
import com.example.connecthub.core.designsystem.component.ConnectHubEmptyState
import com.example.connecthub.core.designsystem.component.ConnectHubErrorState
import com.example.connecthub.core.designsystem.component.ConnectHubLoadingState
import com.example.connecthub.core.designsystem.component.ConnectHubTextField

@Composable
fun ChatListRoute(
    onOpenConversation: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChatListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ChatListScreen(
        uiState = uiState,
        onQueryChange = viewModel::onQueryChange,
        onRetry = viewModel::refresh,
        onOpenConversation = onOpenConversation,
        modifier = modifier,
    )
}

@Composable
fun ChatListScreen(
    uiState: ChatListUiState,
    onQueryChange: (String) -> Unit,
    onRetry: () -> Unit,
    onOpenConversation: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    when {
        uiState.isLoading -> ConnectHubLoadingState(
            message = "Loading conversations",
            modifier = modifier.fillMaxSize(),
        )

        uiState.errorMessage != null && uiState.conversations.isEmpty() -> ConnectHubErrorState(
            title = "Chats unavailable",
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
                ConnectHubTextField(
                    value = uiState.query,
                    onValueChange = onQueryChange,
                    label = "Search conversations",
                    leadingIcon = Icons.Outlined.Search,
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

            if (uiState.visibleConversations.isEmpty()) {
                item {
                    ConnectHubEmptyState(
                        title = "No conversations",
                        message = "Search another name or start from the seeded chat list.",
                        icon = Icons.Outlined.ChatBubbleOutline,
                    )
                }
            } else {
                items(
                    items = uiState.visibleConversations,
                    key = { it.id },
                ) { conversation ->
                    ListItem(
                        headlineContent = { Text(text = conversation.participantName) },
                        supportingContent = { Text(text = conversation.lastMessage) },
                        leadingContent = { ConnectHubAvatar(name = conversation.participantName) },
                        trailingContent = {
                            if (conversation.unreadCount > 0) {
                                Badge { Text(text = conversation.unreadCount.toString()) }
                            }
                        },
                        modifier = Modifier.clickable { onOpenConversation(conversation.id) },
                    )
                }
            }
        }
    }
}
