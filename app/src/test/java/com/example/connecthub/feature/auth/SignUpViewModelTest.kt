package com.example.connecthub.feature.auth

import com.example.connecthub.core.testing.MainDispatcherRule
import com.example.connecthub.domain.usecase.SignUpUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SignUpViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fakeRepository = FakeAuthRepository()
    private val viewModel = SignUpViewModel(SignUpUseCase(fakeRepository))

    @Test
    fun `sign up with invalid form exposes validation errors`() = runTest {
        viewModel.signUp()

        val state = viewModel.uiState.value
        assertEquals("Name is required", state.nameError)
        assertEquals("Email is required", state.emailError)
        assertEquals("Password is required", state.passwordError)
        assertEquals("Confirm your password", state.confirmPasswordError)
        assertFalse(state.isLoading)
    }

    @Test
    fun `sign up success stores session and emits navigation effect`() = runTest {
        viewModel.onNameChange("Alex Morgan")
        viewModel.onEmailChange("alex@connecthub.dev")
        viewModel.onPasswordChange("password")
        viewModel.onConfirmPasswordChange("password")

        viewModel.signUp()
        advanceUntilIdle()

        assertEquals("Alex Morgan", fakeRepository.lastSignUpName)
        assertNotNull(fakeRepository.observeSession().first())
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(SignUpEffect.NavigateToHome, viewModel.effects.first())
    }

    @Test
    fun `sign up failure exposes error and snackbar effect`() = runTest {
        fakeRepository.shouldThrow = true
        viewModel.onNameChange("Alex Morgan")
        viewModel.onEmailChange("alex@connecthub.dev")
        viewModel.onPasswordChange("password")
        viewModel.onConfirmPasswordChange("password")

        viewModel.signUp()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Sign up failed", state.generalError)
        val effect = viewModel.effects.first()
        assertTrue(effect is SignUpEffect.ShowSnackbar)
        assertEquals("Sign up failed", (effect as SignUpEffect.ShowSnackbar).message)
    }
}
