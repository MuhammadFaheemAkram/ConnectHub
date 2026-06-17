package com.example.connecthub.domain.repository

import com.example.connecthub.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface FeedRepository {
    fun observeFeed(): Flow<List<Post>>
    suspend fun refreshFeed(page: Int = 1): Boolean
    suspend fun likePost(postId: String)
    suspend fun bookmarkPost(postId: String)
}
