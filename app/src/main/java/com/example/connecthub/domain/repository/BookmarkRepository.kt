package com.example.connecthub.domain.repository

import com.example.connecthub.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {
    fun observeBookmarks(): Flow<List<Post>>
    suspend fun removeBookmark(postId: String)
}
