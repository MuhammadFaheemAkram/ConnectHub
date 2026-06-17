package com.example.connecthub.feature.profile

import com.example.connecthub.core.testing.MainDispatcherRule
import com.example.connecthub.domain.usecase.EnsureProfileUseCase
import com.example.connecthub.domain.usecase.ObserveProfileUseCase
import com.example.connecthub.domain.usecase.UpdateProfileUseCase
import com.example.connecthub.profile.FakeProfileRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EditProfileViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `blank name shows validation error`() = runTest {
        val viewModel = viewModel(FakeProfileRepository())
        advanceUntilIdle()

        viewModel.onNameChange("")
        viewModel.saveProfile()

        assertEquals("Name is required", viewModel.uiState.value.nameError)
    }

    @Test
    fun `save profile updates repository and emits navigation effect`() = runTest {
        val repository = FakeProfileRepository()
        val viewModel = viewModel(repository)
        advanceUntilIdle()

        viewModel.onNameChange("Alex Updated")
        viewModel.onBioChange("Still building Android apps.")
        viewModel.onAvatarUrlChange("https://example.com/avatar.png")
        viewModel.saveProfile()
        advanceUntilIdle()

        assertEquals("Alex Updated", repository.updatedName)
        assertEquals("Still building Android apps.", repository.updatedBio)
        assertEquals("https://example.com/avatar.png", repository.updatedAvatarUrl)
        assertTrue(viewModel.effects.first() is EditProfileEffect.NavigateBack)
    }

    private fun viewModel(repository: FakeProfileRepository): EditProfileViewModel {
        return EditProfileViewModel(
            ensureProfileUseCase = EnsureProfileUseCase(repository),
            observeProfileUseCase = ObserveProfileUseCase(repository),
            updateProfileUseCase = UpdateProfileUseCase(repository),
        )
    }
}
