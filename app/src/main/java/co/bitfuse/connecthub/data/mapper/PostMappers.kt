package co.bitfuse.connecthub.data.mapper

import co.bitfuse.connecthub.data.feed.PostDto
import co.bitfuse.connecthub.data.feed.PostEntity
import co.bitfuse.connecthub.domain.model.Post
import co.bitfuse.connecthub.domain.model.User

fun PostDto.toEntity(
    page: Int,
    isBookmarkedOverride: Boolean? = null,
): PostEntity = PostEntity(
    id = id,
    authorId = author.id,
    authorName = author.name,
    authorAvatarUrl = author.avatarUrl,
    authorBio = author.bio,
    authorFollowersCount = author.followersCount,
    authorFollowingCount = author.followingCount,
    content = content,
    imageUrl = imageUrl,
    createdAt = createdAt,
    likeCount = likeCount,
    commentCount = commentCount,
    isLiked = isLiked,
    isBookmarked = isBookmarkedOverride ?: isBookmarked,
    page = page,
)

fun PostEntity.toDomain(): Post = Post(
    id = id,
    author = User(
        id = authorId,
        name = authorName,
        avatarUrl = authorAvatarUrl,
        bio = authorBio,
        followersCount = authorFollowersCount,
        followingCount = authorFollowingCount,
    ),
    content = content,
    imageUrl = imageUrl,
    createdAt = createdAt,
    likeCount = likeCount,
    commentCount = commentCount,
    isLiked = isLiked,
    isBookmarked = isBookmarked,
)
