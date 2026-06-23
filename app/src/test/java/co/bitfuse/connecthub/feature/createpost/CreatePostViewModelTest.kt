package co.bitfuse.connecthub.feature.createpost

import co.bitfuse.connecthub.core.testing.MainDispatcherRule
import co.bitfuse.connecthub.domain.usecase.CreatePostUseCase
import co.bitfuse.connecthub.post.FakePostRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CreatePostViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `submit with blank content exposes validation error`() = runTest {
        val viewModel = CreatePostViewModel(CreatePostUseCase(FakePostRepository()))

        viewModel.submit()

        assertEquals("Post text is required", viewModel.uiState.value.contentError)
    }

    @Test
    fun `submit with invalid image url exposes validation error`() = runTest {
        val viewModel = CreatePostViewModel(CreatePostUseCase(FakePostRepository()))
        viewModel.onContentChange("A new post")
        viewModel.onImageUrlChange("not-a-url")

        viewModel.submit()

        assertEquals("Use a valid image URL", viewModel.uiState.value.imageUrlError)
    }

    @Test
    fun `submit success creates post and emits navigate back effect`() = runTest {
        val repository = FakePostRepository()
        val viewModel = CreatePostViewModel(CreatePostUseCase(repository))
        viewModel.onContentChange("A new post")

        viewModel.submit()
        advanceUntilIdle()

        assertEquals("A new post", repository.createdContent)
        assertFalse(viewModel.uiState.value.isSubmitting)
        assertTrue(viewModel.effects.first() is CreatePostEffect.NavigateBack)
    }
}
