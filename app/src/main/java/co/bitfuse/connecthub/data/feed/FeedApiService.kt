package co.bitfuse.connecthub.data.feed

interface FeedApiService {
    suspend fun getFeed(page: Int): List<PostDto>
    suspend fun getPostDetails(postId: String): PostDto
    suspend fun likePost(postId: String): PostDto
    suspend fun bookmarkPost(postId: String): PostDto
    suspend fun createPost(request: CreatePostRequestDto): PostDto
}
