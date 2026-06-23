package co.bitfuse.connecthub.feature.search

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
import androidx.compose.material.icons.outlined.PersonSearch
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.bitfuse.connecthub.core.designsystem.component.ConnectHubAvatar
import co.bitfuse.connecthub.core.designsystem.component.ConnectHubEmptyState
import co.bitfuse.connecthub.core.designsystem.component.ConnectHubLoadingState
import co.bitfuse.connecthub.core.designsystem.component.ConnectHubPostCard
import co.bitfuse.connecthub.core.designsystem.component.ConnectHubTextField

@Composable
fun SearchRoute(
    onOpenPost: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SearchScreen(
        uiState = uiState,
        onQueryChange = viewModel::onQueryChange,
        onSelectRecentSearch = viewModel::selectRecentSearch,
        onClearRecentSearches = viewModel::clearRecentSearches,
        onOpenPost = onOpenPost,
        modifier = modifier,
    )
}

@Composable
fun SearchScreen(
    uiState: SearchUiState,
    onQueryChange: (String) -> Unit,
    onSelectRecentSearch: (String) -> Unit,
    onClearRecentSearches: () -> Unit,
    onOpenPost: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            ConnectHubTextField(
                value = uiState.query,
                onValueChange = onQueryChange,
                label = "Search posts and people",
                leadingIcon = Icons.Outlined.Search,
                modifier = Modifier.fillMaxWidth(),
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

        when {
            uiState.isSearching -> item {
                ConnectHubLoadingState(message = "Searching")
            }

            !uiState.hasQuery -> {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = "Recent searches",
                            style = MaterialTheme.typography.titleMedium,
                        )
                        if (uiState.recentSearches.isNotEmpty()) {
                            TextButton(onClick = onClearRecentSearches) {
                                Text(text = "Clear")
                            }
                        }
                    }
                }
                if (uiState.recentSearches.isEmpty()) {
                    item {
                        ConnectHubEmptyState(
                            title = "No recent searches",
                            message = "Search for posts, people, or topics.",
                            icon = Icons.Outlined.PersonSearch,
                        )
                    }
                } else {
                    items(uiState.recentSearches) { search ->
                        AssistChip(
                            onClick = { onSelectRecentSearch(search) },
                            label = { Text(text = search) },
                        )
                    }
                }
            }

            !uiState.hasResults -> item {
                ConnectHubEmptyState(
                    title = "No results",
                    message = "Try another name, topic, or phrase.",
                    icon = Icons.Outlined.PersonSearch,
                )
            }

            else -> {
                if (uiState.users.isNotEmpty()) {
                    item {
                        Text(
                            text = "People",
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                    items(
                        items = uiState.users,
                        key = { it.id },
                    ) { user ->
                        ListItem(
                            headlineContent = { Text(text = user.name) },
                            supportingContent = { Text(text = user.bio) },
                            leadingContent = { ConnectHubAvatar(name = user.name) },
                        )
                    }
                }

                if (uiState.posts.isNotEmpty()) {
                    item {
                        Text(
                            text = "Posts",
                            style = MaterialTheme.typography.titleMedium,
                        )
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
                        )
                    }
                }
            }
        }
    }
}
