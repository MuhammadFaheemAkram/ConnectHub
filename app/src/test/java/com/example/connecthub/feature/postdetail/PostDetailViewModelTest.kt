package com.example.connecthub.feature.postdetail

import com.example.connecthub.comment.FakeCommentRepository
import com.example.connecthub.core.testing.MainDispatcherRule
import com.example.connecthub.domain.usecase.AddCommentUseCase
import com.example.connecthub.domain.usecase.BookmarkPostUseCase
import com.example.connecthub.domain.usecase.GetPostDetailsUseCase
import com.example.connecthub.domain.usecase.LikePostUseCase
import com.example.connecthub.domain.usecase.ObserveCommentsUseCase
import com.example.connecthub.domain.usecase.RefreshCommentsUseCase
import com.example.connecthub.domain.usecase.RefreshPostDetailsUseCase
import com.example.connecthub.feed.FakeFeedRepository
import com.example.connecthub.post.FakePostRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PostDetailViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `load observes post and comments preview`() = runTest {
        val commentsRepository = FakeCommentRepository(
            initialComments = listOf(FakeCommentRepository.sampleComment()),
        )
        val viewModel = viewModel(
            postRepository = FakePostRepository(),
            commentsRepository = commentsRepository,
        )

        viewModel.load("post-1")
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertNotNull(viewModel.uiState.value.post)
        assertEquals(1, viewModel.uiState.value.commentsPreview.size)
    }

    @Test
    fun `add comment clears detail draft`() = runTest {
        val commentsRepository = FakeCommentRepository()
        val viewModel = viewModel(
            postRepository = FakePostRepository(),
            commentsRepository = commentsRepository,
        )

        viewModel.load("post-1")
        advanceUntilIdle()
        viewModel.onCommentDraftChange("Nice")
        viewModel.addComment()
        advanceUntilIdle()

        assertEquals("", viewModel.uiState.value.commentDraft)
        assertEquals("Nice", commentsRepository.addedText)
    }

    private fun viewModel(
        postRepository: FakePostRepository,
        commentsRepository: FakeCommentRepository,
    ): PostDetailViewModel {
        val feedRepository = FakeFeedRepository()
        return PostDetailViewModel(
            getPostDetailsUseCase = GetPostDetailsUseCase(postRepository),
            refreshPostDetailsUseCase = RefreshPostDetailsUseCase(postRepository),
            observeCommentsUseCase = ObserveCommentsUseCase(commentsRepository),
            refreshCommentsUseCase = RefreshCommentsUseCase(commentsRepository),
            addCommentUseCase = AddCommentUseCase(commentsRepository),
            likePostUseCase = LikePostUseCase(feedRepository),
            bookmarkPostUseCase = BookmarkPostUseCase(feedRepository),
        )
    }
}
