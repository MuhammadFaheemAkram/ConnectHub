package com.example.connecthub.data.comment

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.connecthub.data.feed.PostEntity

@Entity(
    tableName = "comments",
    foreignKeys = [
        ForeignKey(
            entity = PostEntity::class,
            parentColumns = ["id"],
            childColumns = ["postId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(value = ["postId"])],
)
data class CommentEntity(
    @PrimaryKey val id: String,
    val postId: String,
    val authorId: String,
    val authorName: String,
    val authorAvatarUrl: String,
    val authorBio: String,
    val authorFollowersCount: Int,
    val authorFollowingCount: Int,
    val text: String,
    val createdAt: Long,
    val isOwnComment: Boolean,
)
