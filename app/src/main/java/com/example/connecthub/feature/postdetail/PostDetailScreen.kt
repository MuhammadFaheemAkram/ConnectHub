package com.example.connecthub.feature.postdetail

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
import com.example.connecthub.core.designsystem.component.ConnectHubButton
import com.example.connecthub.core.designsystem.component.ConnectHubEmptyState
import com.example.connecthub.core.designsystem.component.ConnectHubErrorState
import com.example.connecthub.core.designsystem.component.ConnectHubLoadingState
import com.example.connecthub.core.designsystem.component.ConnectHubOutlinedButton
import com.example.connecthub.core.designsystem.component.ConnectHubPostCard
import com.example.connecthub.feature.comments.CommentUiModel

@Composable
fun PostDetailScreen(
    postId: String,
    onOpenComments: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PostDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(postId) {
        viewModel.load(postId)
    }

    PostDetailContent(
        uiState = uiState,
        onRetry = { viewModel.refresh(postId) },
        onLikeClick = viewModel::likePost,
        onBookmarkClick = viewModel::bookmarkPost,
        onCommentDraftChange = viewModel::onCommentDraftChange,
        onAddComment = viewModel::addComment,
        onOpenComments = onOpenComments,
        modifier = modifier,
    )
}

@Composable
fun PostDetailContent(
    uiState: PostDetailUiState,
    onRetry: () -> Unit,
    onLikeClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    onCommentDraftChange: (String) -> Unit,
    onAddComment: () -> Unit,
    onOpenComments: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when {
        uiState.isLoading -> ConnectHubLoadingState(
            message = "Loading post",
            modifier = modifier.fillMaxSize(),
        )

        uiState.post == null -> ConnectHubErrorState(
            title = "Post unavailable",
            message = uiState.errorMessage ?: "This post could not be loaded.",
            onRetry = onRetry,
            modifier = modifier.fillMaxSize(),
        )

        else -> LazyColumn(
            modifier = modifier.padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                ConnectHubPostCard(
                    authorName = uiState.post.authorName,
                    authorHeadline = uiState.post.authorHeadline,
                    timeAgo = uiState.post.timeAgo,
                    content = uiState.post.content,
                    imageUrl = uiState.post.imageUrl,
                    likeCount = uiState.post.likeCount,
                    commentCount = uiState.post.commentCount,
                    isLiked = uiState.post.isLiked,
                    isBookmarked = uiState.post.isBookmarked,
                    onLikeClick = onLikeClick,
                    onBookmarkClick = onBookmarkClick,
                    onCommentsClick = onOpenComments,
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

            item {
                AddCommentBox(
                    draft = uiState.commentDraft,
                    isSubmitting = uiState.isSubmittingComment,
                    onDraftChange = onCommentDraftChange,
                    onAddComment = onAddComment,
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "Comments",
                        style = MaterialTheme.typography.titleMedium,
                    )
                    ConnectHubOutlinedButton(
                        text = "View all",
                        onClick = onOpenComments,
                    )
                }
            }

            if (uiState.commentsPreview.isEmpty()) {
                item {
                    ConnectHubEmptyState(
                        title = "No comments yet",
                        message = "Be the first to respond.",
                        icon = Icons.AutoMirrored.Outlined.Comment,
                    )
                }
            } else {
                items(
                    items = uiState.commentsPreview,
                    key = { it.id },
                ) { comment ->
                    CommentPreviewRow(comment = comment)
                }
            }
        }
    }
}

@Composable
private fun AddCommentBox(
    draft: String,
    isSubmitting: Boolean,
    onDraftChange: (String) -> Unit,
    onAddComment: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = draft,
            onValueChange = onDraftChange,
            label = { Text(text = "Add a comment") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
        )
        ConnectHubButton(
            text = "Comment",
            onClick = onAddComment,
            enabled = draft.isNotBlank(),
            isLoading = isSubmitting,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun CommentPreviewRow(
    comment: CommentUiModel,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text(
            text = comment.authorName,
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = comment.text,
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            text = comment.timeAgo,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
