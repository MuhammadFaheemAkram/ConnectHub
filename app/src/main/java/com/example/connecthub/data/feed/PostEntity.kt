package com.example.connecthub.data.feed

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey val id: String,
    val authorId: String,
    val authorName: String,
    val authorAvatarUrl: String,
    val authorBio: String,
    val authorFollowersCount: Int,
    val authorFollowingCount: Int,
    val content: String,
    val imageUrl: String?,
    val createdAt: Long,
    val likeCount: Int,
    val commentCount: Int,
    val isLiked: Boolean,
    val isBookmarked: Boolean,
    val page: Int,
)
