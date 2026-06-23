package co.bitfuse.connecthub.domain.usecase

import co.bitfuse.connecthub.comment.FakeCommentRepository
import co.bitfuse.connecthub.post.FakePostRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class Phase4UseCaseTest {
    @Test
    fun `CreatePostUseCase trims content and blank image url`() = runTest {
        val repository = FakePostRepository()

        val post = CreatePostUseCase(repository)(
            content = "  hello phase four  ",
            imageUrl = "   ",
        )

        assertEquals("hello phase four", repository.createdContent)
        assertEquals("hello phase four", post.content)
        assertEquals(null, post.imageUrl)
    }

    @Test
    fun `AddCommentUseCase trims comment text`() = runTest {
        val repository = FakeCommentRepository()

        val comment = AddCommentUseCase(repository)(
            postId = "post-1",
            text = "  nice work  ",
        )

        assertEquals("nice work", repository.addedText)
        assertEquals("nice work", comment.text)
    }
}
