package co.bitfuse.connecthub.feature.comments

import co.bitfuse.connecthub.comment.FakeCommentRepository
import co.bitfuse.connecthub.core.testing.MainDispatcherRule
import co.bitfuse.connecthub.domain.usecase.AddCommentUseCase
import co.bitfuse.connecthub.domain.usecase.DeleteCommentUseCase
import co.bitfuse.connecthub.domain.usecase.ObserveCommentsUseCase
import co.bitfuse.connecthub.domain.usecase.RefreshCommentsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CommentsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `load observes comments and add comment clears draft`() = runTest {
        val repository = FakeCommentRepository(
            initialComments = listOf(FakeCommentRepository.sampleComment()),
        )
        val viewModel = viewModel(repository)

        viewModel.load("post-1")
        advanceUntilIdle()
        viewModel.onDraftChange("New comment")
        viewModel.addComment()
        advanceUntilIdle()

        assertEquals(2, viewModel.uiState.value.comments.size)
        assertEquals("", viewModel.uiState.value.draft)
        assertFalse(viewModel.uiState.value.isSubmitting)
    }

    @Test
    fun `delete own comment delegates to repository`() = runTest {
        val repository = FakeCommentRepository(
            initialComments = listOf(
                FakeCommentRepository.sampleComment(id = "comment-1", isOwnComment = true),
            ),
        )
        val viewModel = viewModel(repository)

        viewModel.load("post-1")
        advanceUntilIdle()
        viewModel.deleteComment("comment-1")
        advanceUntilIdle()

        assertEquals("comment-1", repository.deletedCommentId)
        assertEquals(emptyList<CommentUiModel>(), viewModel.uiState.value.comments)
    }

    private fun viewModel(repository: FakeCommentRepository): CommentsViewModel {
        return CommentsViewModel(
            observeCommentsUseCase = ObserveCommentsUseCase(repository),
            refreshCommentsUseCase = RefreshCommentsUseCase(repository),
            addCommentUseCase = AddCommentUseCase(repository),
            deleteCommentUseCase = DeleteCommentUseCase(repository),
        )
    }
}
