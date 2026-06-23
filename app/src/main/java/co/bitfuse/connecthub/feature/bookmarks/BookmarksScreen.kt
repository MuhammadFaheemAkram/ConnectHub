package co.bitfuse.connecthub.feature.bookmarks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.bitfuse.connecthub.core.designsystem.component.ConnectHubEmptyState
import co.bitfuse.connecthub.core.designsystem.component.ConnectHubErrorState
import co.bitfuse.connecthub.core.designsystem.component.ConnectHubLoadingState
import co.bitfuse.connecthub.core.designsystem.component.ConnectHubPostCard

@Composable
fun BookmarksRoute(
    onOpenPost: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BookmarksViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BookmarksScreen(
        uiState = uiState,
        onOpenPost = onOpenPost,
        onRemoveBookmark = viewModel::removeBookmark,
        modifier = modifier,
    )
}

@Composable
fun BookmarksScreen(
    uiState: BookmarksUiState,
    onOpenPost: (String) -> Unit,
    onRemoveBookmark: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    when {
        uiState.isLoading -> ConnectHubLoadingState(
            message = "Loading saved posts",
            modifier = modifier.fillMaxSize(),
        )

        uiState.errorMessage != null && uiState.posts.isEmpty() -> ConnectHubErrorState(
            title = "Bookmarks unavailable",
            message = uiState.errorMessage,
            onRetry = {},
            modifier = modifier.fillMaxSize(),
        )

        uiState.posts.isEmpty() -> ConnectHubEmptyState(
            title = "No saved posts",
            message = "Bookmark posts from the feed to keep them available offline.",
            icon = Icons.Outlined.BookmarkBorder,
            modifier = modifier.fillMaxSize(),
        )

        else -> LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            uiState.errorMessage?.let { message ->
                item {
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }

            items(
                items = uiState.posts,
                key = { it.id },
            ) { post ->
                ConnectHubPostCard(
                    authorName = post.authorName,
                    authorHeadline = post.authorHeadline,
                    timeAgo = post.timeAgo,
                    content = post.content,
                    imageUrl = post.imageUrl,
                    likeCount = post.likeCount,
                    commentCount = post.commentCount,
                    isLiked = post.isLiked,
                    isBookmarked = post.isBookmarked,
                    onClick = { onOpenPost(post.id) },
                    onBookmarkClick = { onRemoveBookmark(post.id) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
