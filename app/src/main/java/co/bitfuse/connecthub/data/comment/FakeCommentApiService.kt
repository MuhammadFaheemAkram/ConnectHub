package co.bitfuse.connecthub.data.comment

import co.bitfuse.connecthub.core.network.FakeNetworkConfig
import co.bitfuse.connecthub.data.auth.UserDto
import javax.inject.Inject
import kotlinx.coroutines.delay

class FakeCommentApiService @Inject constructor() : CommentApiService {
    private val commentsByPostId = seedComments().mapValues { it.value.toMutableList() }.toMutableMap()

    override suspend fun getComments(postId: String): List<CommentDto> {
        delay(FakeNetworkConfig.defaultDelayMillis / 2)
        maybeThrowSimulatedError()
        return commentsByPostId[postId].orEmpty().sortedBy { it.createdAt }
    }

    override suspend fun addComment(postId: String, text: String): CommentDto {
        delay(FakeNetworkConfig.defaultDelayMillis / 2)
        maybeThrowSimulatedError()
        val comment = CommentDto(
            id = "comment-${System.currentTimeMillis()}",
            postId = postId,
            author = alex,
            text = text,
            createdAt = System.currentTimeMillis(),
            isOwnComment = true,
        )
        commentsByPostId.getOrPut(postId) { mutableListOf() }.add(comment)
        return comment
    }

    private fun maybeThrowSimulatedError() {
        if (FakeNetworkConfig.errorSimulationEnabled) {
            error("Fake comment service is simulating a network failure.")
        }
    }

    companion object {
        private val omar = UserDto(
            id = "user-omar",
            name = "Omar Malik",
            email = "omar@connecthub.dev",
            avatarUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e",
            bio = "Product Designer",
            followersCount = 5100,
            followingCount = 440,
        )
        private val nadia = UserDto(
            id = "user-nadia",
            name = "Nadia Khan",
            email = "nadia@connecthub.dev",
            avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb",
            bio = "Engineering Manager",
            followersCount = 12700,
            followingCount = 540,
        )
        private val alex = UserDto(
            id = "user-alex",
            name = "Alex Morgan",
            email = "alex@connecthub.dev",
            avatarUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9",
            bio = "Mobile Lead",
            followersCount = 1240,
            followingCount = 318,
        )

        private fun seedComments(): Map<String, List<CommentDto>> {
            val now = System.currentTimeMillis()
            return mapOf(
                "post-001" to listOf(
                    CommentDto(
                        id = "comment-001",
                        postId = "post-001",
                        author = omar,
                        text = "This cache-first approach makes the feed feel immediate.",
                        createdAt = now - 8 * 60_000,
                        isOwnComment = false,
                    ),
                    CommentDto(
                        id = "comment-002",
                        postId = "post-001",
                        author = nadia,
                        text = "The repository boundary is doing useful work here.",
                        createdAt = now - 5 * 60_000,
                        isOwnComment = false,
                    ),
                ),
                "post-002" to listOf(
                    CommentDto(
                        id = "comment-003",
                        postId = "post-002",
                        author = alex,
                        text = "The card hierarchy already feels scannable.",
                        createdAt = now - 30 * 60_000,
                        isOwnComment = true,
                    ),
                ),
            )
        }
    }
}
