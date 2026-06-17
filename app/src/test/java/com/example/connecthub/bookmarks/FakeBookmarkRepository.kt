package com.example.connecthub.bookmarks

import com.example.connecthub.domain.model.Post
import com.example.connecthub.domain.repository.BookmarkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeBookmarkRepository(
    initialPosts: List<Post> = emptyList(),
) : BookmarkRepository {
    private val posts = MutableStateFlow(initialPosts)
    var removedPostId: String? = null

    override fun observeBookmarks(): Flow<List<Post>> = posts

    override suspend fun removeBookmark(postId: String) {
        removedPostId = postId
        posts.value = posts.value.filterNot { it.id == postId }
    }
}
