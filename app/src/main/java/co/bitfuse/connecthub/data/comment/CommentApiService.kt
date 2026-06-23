package co.bitfuse.connecthub.data.comment

interface CommentApiService {
    suspend fun getComments(postId: String): List<CommentDto>
    suspend fun addComment(postId: String, text: String): CommentDto
}
