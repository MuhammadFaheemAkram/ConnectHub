package com.example.connecthub.feature.comments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Comment
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.connecthub.core.designsystem.component.ConnectHubAvatar
import com.example.connecthub.core.designsystem.component.ConnectHubButton
import com.example.connecthub.core.designsystem.component.ConnectHubEmptyState
import com.example.connecthub.core.designsystem.component.ConnectHubLoadingState

@Composable
fun CommentsScreen(
    postId: String,
    modifier: Modifier = Modifier,
    viewModel: CommentsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(postId) {
        viewModel.load(postId)
    }

    CommentsContent(
        uiState = uiState,
        onDraftChange = viewModel::onDraftChange,
        onAddComment = viewModel::addComment,
        onDeleteComment = viewModel::deleteComment,
        modifier = modifier,
    )
}

@Composable
fun CommentsContent(
    uiState: CommentsUiState,
    onDraftChange: (String) -> Unit,
    onAddComment: () -> Unit,
    onDeleteComment: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
    ) {
        if (uiState.errorMessage != null) {
            Text(
                text = uiState.errorMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp),
            )
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            when {
                uiState.isLoading -> item {
                    ConnectHubLoadingState(message = "Loading comments")
                }

                uiState.comments.isEmpty() -> item {
                    ConnectHubEmptyState(
                        title = "No comments",
                        message = "Start the conversation.",
                        icon = Icons.AutoMirrored.Outlined.Comment,
                    )
                }

                else -> items(
                    items = uiState.comments,
                    key = { it.id },
                ) { comment ->
                    ListItem(
                        headlineContent = { Text(text = comment.authorName) },
                        supportingContent = {
                            Column {
                                Text(text = comment.text)
                                Text(
                                    text = comment.timeAgo,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        },
                        leadingContent = { ConnectHubAvatar(name = comment.authorName) },
                        trailingContent = {
                            if (comment.isOwnComment) {
                                IconButton(onClick = { onDeleteComment(comment.id) }) {
                                    Icon(
                                        imageVector = Icons.Outlined.Delete,
                                        contentDescription = "Delete comment",
                                    )
                                }
                            }
                        },
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            OutlinedTextField(
                value = uiState.draft,
                onValueChange = onDraftChange,
                label = { Text(text = "Comment") },
                modifier = Modifier.weight(1f),
                singleLine = true,
            )
            ConnectHubButton(
                text = "Send",
                onClick = onAddComment,
                enabled = uiState.draft.isNotBlank(),
                isLoading = uiState.isSubmitting,
            )
        }
    }
}
