package com.example.connecthub.feed

import com.example.connecthub.data.feed.CreatePostRequestDto
import com.example.connecthub.data.feed.FeedApiService
import com.example.connecthub.data.feed.PostDto

class FakeFeedApi(
    private val pages: MutableMap<Int, List<PostDto>> = mutableMapOf(
        1 to listOf(FeedTestData.postDto(id = "post-1")),
    ),
) : FeedApiService {
    var shouldThrow = false

    override suspend fun getFeed(page: Int): List<PostDto> {
        if (shouldThrow) error("Feed failed")
        return pages[page].orEmpty()
    }

    override suspend fun getPostDetails(postId: String): PostDto {
        if (shouldThrow) error("Post failed")
        return pages.values.flatten().first { it.id == postId }
    }

    override suspend fun likePost(postId: String): PostDto {
        if (shouldThrow) error("Like failed")
        val post = getPostDetails(postId)
        val liked = !post.isLiked
        return post.copy(
            isLiked = liked,
            likeCount = (post.likeCount + if (liked) 1 else -1).coerceAtLeast(0),
        )
    }

    override suspend fun bookmarkPost(postId: String): PostDto {
        if (shouldThrow) error("Bookmark failed")
        val post = getPostDetails(postId)
        return post.copy(isBookmarked = !post.isBookmarked)
    }

    override suspend fun createPost(request: CreatePostRequestDto): PostDto {
        if (shouldThrow) error("Create post failed")
        return FeedTestData.postDto(id = "created-post")
    }
}
