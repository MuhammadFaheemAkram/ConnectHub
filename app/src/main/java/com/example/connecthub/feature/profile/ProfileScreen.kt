package com.example.connecthub.feature.profile

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
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.connecthub.core.designsystem.component.ConnectHubAvatar
import com.example.connecthub.core.designsystem.component.ConnectHubEmptyState
import com.example.connecthub.core.designsystem.component.ConnectHubErrorState
import com.example.connecthub.core.designsystem.component.ConnectHubLoadingState
import com.example.connecthub.core.designsystem.component.ConnectHubOutlinedButton
import com.example.connecthub.core.designsystem.component.ConnectHubPostCard
import com.example.connecthub.domain.model.User
import com.example.connecthub.feature.feed.PostUiModel

@Composable
fun ProfileRoute(
    onEditProfile: () -> Unit,
    onOpenPost: (String) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ProfileScreen(
        uiState = uiState,
        onEditProfile = onEditProfile,
        onOpenPost = onOpenPost,
        onLogout = onLogout,
        modifier = modifier,
    )
}

@Composable
fun ProfileScreen(
    uiState: ProfileUiState,
    onEditProfile: () -> Unit,
    onOpenPost: (String) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when {
        uiState.isLoading -> ConnectHubLoadingState(
            message = "Loading profile",
            modifier = modifier.fillMaxSize(),
        )

        uiState.user == null -> ConnectHubErrorState(
            title = "Profile unavailable",
            message = uiState.errorMessage ?: "Your profile could not be loaded.",
            onRetry = {},
            modifier = modifier.fillMaxSize(),
        )

        else -> ProfileContent(
            user = uiState.user,
            posts = uiState.posts,
            errorMessage = uiState.errorMessage,
            onEditProfile = onEditProfile,
            onOpenPost = onOpenPost,
            onLogout = onLogout,
            modifier = modifier,
        )
    }
}

@Composable
private fun ProfileContent(
    user: User,
    posts: List<PostUiModel>,
    errorMessage: String?,
    onEditProfile: () -> Unit,
    onOpenPost: (String) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            ProfileHeader(
                user = user,
                onEditProfile = onEditProfile,
                onLogout = onLogout,
            )
        }

        errorMessage?.let { message ->
            item {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }

        item {
            Text(
                text = "Posts",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
        }

        if (posts.isEmpty()) {
            item {
                ConnectHubEmptyState(
                    title = "No profile posts",
                    message = "Posts you create will appear here.",
                    icon = Icons.AutoMirrored.Outlined.Article,
                )
            }
        } else {
            items(
                items = posts,
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
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun ProfileHeader(
    user: User,
    onEditProfile: () -> Unit,
    onLogout: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        ConnectHubAvatar(name = user.name, size = 84.dp)
        Text(
            text = user.name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = user.bio,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            ProfileStat(label = "Followers", value = user.followersCount.compactCount())
            ProfileStat(label = "Following", value = user.followingCount.compactCount())
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ConnectHubOutlinedButton(
                text = "Edit profile",
                onClick = onEditProfile,
            )
            ConnectHubOutlinedButton(
                text = "Logout",
                onClick = onLogout,
            )
        }
    }
}

@Composable
private fun ProfileStat(
    label: String,
    value: String,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

private fun Int.compactCount(): String {
    return if (this >= 1_000) {
        val whole = this / 1_000
        val tenths = (this % 1_000) / 100
        if (tenths == 0) "${whole}k" else "$whole.${tenths}k"
    } else {
        toString()
    }
}
