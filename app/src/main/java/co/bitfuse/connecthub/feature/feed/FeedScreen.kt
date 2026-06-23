package co.bitfuse.connecthub.feature.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.DynamicFeed
import androidx.compose.material3.FilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.bitfuse.connecthub.core.designsystem.component.ConnectHubEmptyState
import co.bitfuse.connecthub.core.designsystem.component.ConnectHubErrorState
import co.bitfuse.connecthub.core.designsystem.component.ConnectHubLoadingState
import co.bitfuse.connecthub.core.designsystem.component.ConnectHubOutlinedButton
import co.bitfuse.connecthub.core.designsystem.component.ConnectHubPostCard
import co.bitfuse.connecthub.core.testing.TestTags

@Composable
fun FeedRoute(
    onOpenCreatePost: () -> Unit,
    onOpenPost: (String) -> Unit,
    onOpenComments: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FeedViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    FeedScreen(
        uiState = uiState,
        onRefresh = viewModel::refresh,
        onLoadMore = viewModel::loadNextPage,
        onLikePost = viewModel::likePost,
        onBookmarkPost = viewModel::bookmarkPost,
        onOpenCreatePost = onOpenCreatePost,
        onOpenPost = onOpenPost,
        onOpenComments = onOpenComments,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    uiState: FeedUiState,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    onLikePost: (String) -> Unit,
    onBookmarkPost: (String) -> Unit,
    onOpenCreatePost: () -> Unit,
    onOpenPost: (String) -> Unit,
    onOpenComments: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier.fillMaxSize(),
    ) {
        when {
            uiState.isLoading -> {
                ConnectHubLoadingState(
                    message = "Loading your feed",
                    modifier = Modifier.fillMaxSize(),
                )
            }

            uiState.errorMessage != null && uiState.posts.isEmpty() -> {
                ConnectHubErrorState(
                    title = "Feed unavailable",
                    message = uiState.errorMessage,
                    onRetry = onRefresh,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            uiState.posts.isEmpty() -> {
                ConnectHubEmptyState(
                    title = "No posts yet",
                    message = "Create the first post in your network.",
                    icon = Icons.Outlined.DynamicFeed,
                    action = {
                        ConnectHubOutlinedButton(
                            text = "Create post",
                            onClick = onOpenCreatePost,
                        )
                    },
                    modifier = Modifier.fillMaxSize(),
                )
            }

            else -> {
                FeedContent(
                    uiState = uiState,
                    onLoadMore = onLoadMore,
                    onLikePost = onLikePost,
                    onBookmarkPost = onBookmarkPost,
                    onOpenCreatePost = onOpenCreatePost,
                    onOpenPost = onOpenPost,
                    onOpenComments = onOpenComments,
                )
            }
        }
    }
}

@Composable
private fun FeedContent(
    uiState: FeedUiState,
    onLoadMore: () -> Unit,
    onLikePost: (String) -> Unit,
    onBookmarkPost: (String) -> Unit,
    onOpenCreatePost: () -> Unit,
    onOpenPost: (String) -> Unit,
    onOpenComments: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .testTag(TestTags.feedList)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Today",
                    style = MaterialTheme.typography.headlineSmall,
                )
                FilterChip(
                    selected = true,
                    onClick = onOpenCreatePost,
                    label = { Text(text = "Compose") },
                )
            }
        }

        if (uiState.errorMessage != null) {
            item {
                ConnectHubEmptyState(
                    title = "Showing cached posts",
                    message = uiState.errorMessage,
                    icon = Icons.Outlined.CloudOff,
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
                onLikeClick = { onLikePost(post.id) },
                onBookmarkClick = { onBookmarkPost(post.id) },
                onCommentsClick = { onOpenComments(post.id) },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        item {
            if (uiState.canLoadMore) {
                ConnectHubOutlinedButton(
                    text = if (uiState.isLoadingMore) "Loading" else "Load more",
                    onClick = onLoadMore,
                    enabled = !uiState.isLoadingMore,
                    isLoading = uiState.isLoadingMore,
                    modifier = Modifier.fillMaxWidth(),
                )
            } else {
                Text(
                    text = "You are caught up",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 12.dp),
                )
            }
        }
    }
}
