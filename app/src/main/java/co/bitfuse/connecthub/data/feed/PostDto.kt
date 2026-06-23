package co.bitfuse.connecthub.data.feed

import co.bitfuse.connecthub.data.auth.UserDto

data class PostDto(
    val id: String,
    val author: UserDto,
    val content: String,
    val imageUrl: String?,
    val createdAt: Long,
    val likeCount: Int,
    val commentCount: Int,
    val isLiked: Boolean,
    val isBookmarked: Boolean,
)
