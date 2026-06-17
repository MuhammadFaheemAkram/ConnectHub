package com.example.connecthub.feature.auth

import com.example.connecthub.core.testing.MainDispatcherRule
import com.example.connecthub.domain.usecase.LoginUseCase
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
class LoginViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fakeRepository = FakeAuthRepository()
    private val viewModel = LoginViewModel(LoginUseCase(fakeRepository))

    @Test
    fun `login with invalid form exposes validation errors`() = runTest {
        viewModel.login()

        val state = viewModel.uiState.value
        assertEquals("Email is required", state.emailError)
        assertEquals("Password is required", state.passwordError)
        assertFalse(state.isLoading)
    }

    @Test
    fun `login success stores session and emits navigation effect`() = runTest {
        viewModel.onEmailChange("alex@connecthub.dev")
        viewModel.onPasswordChange("password")

        viewModel.login()
        advanceUntilIdle()

        assertEquals("alex@connecthub.dev", fakeRepository.lastLoginEmail)
        assertNotNull(fakeRepository.observeSession().first())
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(LoginEffect.NavigateToHome, viewModel.effects.first())
    }

    @Test
    fun `login failure exposes error and snackbar effect`() = runTest {
        fakeRepository.shouldThrow = true
        viewModel.onEmailChange("alex@connecthub.dev")
        viewModel.onPasswordChange("password")

        viewModel.login()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Auth failed", state.generalError)
        val effect = viewModel.effects.first()
        assertTrue(effect is LoginEffect.ShowSnackbar)
        assertEquals("Auth failed", (effect as LoginEffect.ShowSnackbar).message)
    }
}
