package com.example.connecthub.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.connecthub.domain.usecase.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    private val effectChannel = Channel<SignUpEffect>(Channel.BUFFERED)
    val effects = effectChannel.receiveAsFlow()

    fun onNameChange(name: String) {
        _uiState.update { it.copy(name = name, nameError = null, generalError = null) }
    }

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, emailError = null, generalError = null) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, passwordError = null, generalError = null) }
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _uiState.update {
            it.copy(confirmPassword = confirmPassword, confirmPasswordError = null, generalError = null)
        }
    }

    fun signUp() {
        val state = _uiState.value
        val validation = AuthValidation.validateSignUp(
            name = state.name,
            email = state.email,
            password = state.password,
            confirmPassword = state.confirmPassword,
        )
        if (!validation.isValid) {
            _uiState.update {
                it.copy(
                    nameError = validation.nameError,
                    emailError = validation.emailError,
                    passwordError = validation.passwordError,
                    confirmPasswordError = validation.confirmPasswordError,
                    generalError = null,
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, generalError = null) }
            runCatching {
                signUpUseCase(
                    name = state.name,
                    email = state.email,
                    password = state.password,
                )
            }.onSuccess {
                _uiState.update { it.copy(isLoading = false) }
                effectChannel.send(SignUpEffect.NavigateToHome)
            }.onFailure { throwable ->
                val message = throwable.message ?: "Unable to create account"
                _uiState.update {
                    it.copy(isLoading = false, generalError = message)
                }
                effectChannel.send(SignUpEffect.ShowSnackbar(message))
            }
        }
    }
}
