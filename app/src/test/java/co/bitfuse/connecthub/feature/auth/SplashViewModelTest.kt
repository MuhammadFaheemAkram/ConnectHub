package co.bitfuse.connecthub.feature.auth

import co.bitfuse.connecthub.core.testing.MainDispatcherRule
import co.bitfuse.connecthub.domain.usecase.ObserveSessionUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `session check emits authenticated when session exists`() = runTest {
        val repository = FakeAuthRepository(
            initialSession = FakeAuthRepository.sampleSession(),
        )
        val viewModel = SplashViewModel(ObserveSessionUseCase(repository))

        advanceUntilIdle()

        assertEquals(SplashUiState.Authenticated, viewModel.uiState.value)
    }

    @Test
    fun `session check emits unauthenticated when session is missing`() = runTest {
        val repository = FakeAuthRepository(initialSession = null)
        val viewModel = SplashViewModel(ObserveSessionUseCase(repository))

        advanceUntilIdle()

        assertEquals(SplashUiState.Unauthenticated, viewModel.uiState.value)
    }
}
