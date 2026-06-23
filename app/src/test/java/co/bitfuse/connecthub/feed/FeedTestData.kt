package co.bitfuse.connecthub.feed

import co.bitfuse.connecthub.data.auth.UserDto
import co.bitfuse.connecthub.data.feed.PostDto
import co.bitfuse.connecthub.data.feed.PostEntity
import co.bitfuse.connecthub.domain.model.Post
import co.bitfuse.connecthub.domain.model.User

object FeedTestData {
    fun user(id: String = "user-1"): User = User(
        id = id,
        name = "Maya Chen",
        avatarUrl = "",
        bio = "Android Engineer",
        followersCount = 10,
        followingCount = 4,
    )

    fun userDto(id: String = "user-1"): UserDto = UserDto(
        id = id,
        name = "Maya Chen",
        email = "maya@connecthub.dev",
        avatarUrl = "",
        bio = "Android Engineer",
        followersCount = 10,
        followingCount = 4,
    )

    fun post(
        id: String = "post-1",
        isLiked: Boolean = false,
        isBookmarked: Boolean = false,
        likeCount: Int = 4,
    ): Post = Post(
        id = id,
        author = user(),
        content = "A cached post",
        imageUrl = null,
        createdAt = 1_000L,
        likeCount = likeCount,
        commentCount = 2,
        isLiked = isLiked,
        isBookmarked = isBookmarked,
    )

    fun postDto(
        id: String = "post-1",
        isLiked: Boolean = false,
        isBookmarked: Boolean = false,
        likeCount: Int = 4,
    ): PostDto = PostDto(
        id = id,
        author = userDto(),
        content = "A remote post",
        imageUrl = null,
        createdAt = 1_000L,
        likeCount = likeCount,
        commentCount = 2,
        isLiked = isLiked,
        isBookmarked = isBookmarked,
    )

    fun postEntity(
        id: String = "post-1",
        isLiked: Boolean = false,
        isBookmarked: Boolean = false,
        likeCount: Int = 4,
    ): PostEntity = PostEntity(
        id = id,
        authorId = "user-1",
        authorName = "Maya Chen",
        authorAvatarUrl = "",
        authorBio = "Android Engineer",
        authorFollowersCount = 10,
        authorFollowingCount = 4,
        content = "A cached post",
        imageUrl = null,
        createdAt = 1_000L,
        likeCount = likeCount,
        commentCount = 2,
        isLiked = isLiked,
        isBookmarked = isBookmarked,
        page = 1,
    )
}
