package com.example.connecthub.feed

import com.example.connecthub.data.feed.BookmarkedPostEntity
import com.example.connecthub.data.feed.PostDao
import com.example.connecthub.data.feed.PostEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakePostDao : PostDao {
    private val posts = MutableStateFlow<List<PostEntity>>(emptyList())
    private val bookmarks = LinkedHashMap<String, BookmarkedPostEntity>()

    override fun observeFeed(): Flow<List<PostEntity>> {
        return posts.map { cachedPosts -> cachedPosts.sortedByDescending { it.createdAt } }
    }

    override fun searchPosts(query: String): Flow<List<PostEntity>> {
        return posts.map { cachedPosts ->
            cachedPosts
                .filter {
                    it.content.contains(query, ignoreCase = true) ||
                        it.authorName.contains(query, ignoreCase = true) ||
                        it.authorBio.contains(query, ignoreCase = true)
                }
                .sortedByDescending { it.createdAt }
        }
    }

    override fun observePostsByAuthor(authorId: String): Flow<List<PostEntity>> {
        return posts.map { cachedPosts ->
            cachedPosts
                .filter { it.authorId == authorId }
                .sortedByDescending { it.createdAt }
        }
    }

    override fun observePost(postId: String): Flow<PostEntity?> {
        return posts.map { cachedPosts -> cachedPosts.firstOrNull { it.id == postId } }
    }

    override suspend fun getPostById(postId: String): PostEntity? {
        return posts.value.firstOrNull { it.id == postId }
    }

    override suspend fun getBookmarkedPostIds(): List<String> = bookmarks.keys.toList()

    override suspend fun upsertPosts(posts: List<PostEntity>) {
        posts.forEach { upsertPost(it) }
    }

    override suspend fun upsertPost(post: PostEntity) {
        val existing = posts.value.associateBy { it.id }.toMutableMap()
        existing[post.id] = post
        posts.value = existing.values.toList()
    }

    override suspend fun updateLikeState(postId: String, isLiked: Boolean, likeCount: Int) {
        posts.value = posts.value.map { post ->
            if (post.id == postId) {
                post.copy(isLiked = isLiked, likeCount = likeCount)
            } else {
                post
            }
        }
    }

    override suspend fun updateBookmarkFlag(postId: String, isBookmarked: Boolean) {
        posts.value = posts.value.map { post ->
            if (post.id == postId) post.copy(isBookmarked = isBookmarked) else post
        }
    }

    override suspend fun updateCommentCount(postId: String, delta: Int) {
        posts.value = posts.value.map { post ->
            if (post.id == postId) {
                post.copy(commentCount = (post.commentCount + delta).coerceAtLeast(0))
            } else {
                post
            }
        }
    }

    override suspend fun upsertBookmark(bookmarkedPost: BookmarkedPostEntity) {
        bookmarks[bookmarkedPost.postId] = bookmarkedPost
    }

    override suspend fun deleteBookmark(postId: String) {
        bookmarks.remove(postId)
    }

    override fun observeBookmarkedPosts(): Flow<List<PostEntity>> {
        return posts.map { cachedPosts ->
            cachedPosts
                .filter { it.id in bookmarks.keys }
                .sortedByDescending { bookmarks[it.id]?.bookmarkedAt ?: 0L }
        }
    }
}
