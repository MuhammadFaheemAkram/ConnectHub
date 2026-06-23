package co.bitfuse.connecthub.feature.profile

import co.bitfuse.connecthub.core.testing.MainDispatcherRule
import co.bitfuse.connecthub.domain.usecase.EnsureProfileUseCase
import co.bitfuse.connecthub.domain.usecase.ObserveProfilePostsUseCase
import co.bitfuse.connecthub.domain.usecase.ObserveProfileUseCase
import co.bitfuse.connecthub.feed.FeedTestData
import co.bitfuse.connecthub.profile.FakeProfileRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `profile state loads persisted profile and posts`() = runTest {
        val repository = FakeProfileRepository(
            initialPosts = listOf(FeedTestData.post(id = "post-1")),
        )

        val viewModel = ProfileViewModel(
            ensureProfileUseCase = EnsureProfileUseCase(repository),
            observeProfileUseCase = ObserveProfileUseCase(repository),
            observeProfilePostsUseCase = ObserveProfilePostsUseCase(repository),
        )
        advanceUntilIdle()

        assertTrue(repository.ensuredProfile)
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals("Alex Morgan", viewModel.uiState.value.user?.name)
        assertEquals(listOf("post-1"), viewModel.uiState.value.posts.map { it.id })
    }
}
