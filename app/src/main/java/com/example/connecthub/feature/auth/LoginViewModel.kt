package com.example.connecthub.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.connecthub.domain.usecase.LoginUseCase
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
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val effectChannel = Channel<LoginEffect>(Channel.BUFFERED)
    val effects = effectChannel.receiveAsFlow()

    fun onEmailChange(email: String) {
        _uiState.update {
            it.copy(email = email, emailError = null, generalError = null)
        }
    }

    fun onPasswordChange(password: String) {
        _uiState.update {
            it.copy(password = password, passwordError = null, generalError = null)
        }
    }

    fun login() {
        val state = _uiState.value
        val validation = AuthValidation.validateLogin(
            email = state.email,
            password = state.password,
        )
        if (!validation.isValid) {
            _uiState.update {
                it.copy(
                    emailError = validation.emailError,
                    passwordError = validation.passwordError,
                    generalError = null,
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, generalError = null) }
            runCatching {
                loginUseCase(email = state.email, password = state.password)
            }.onSuccess {
                _uiState.update { it.copy(isLoading = false) }
                effectChannel.send(LoginEffect.NavigateToHome)
            }.onFailure { throwable ->
                val message = throwable.message ?: "Unable to sign in"
                _uiState.update {
                    it.copy(isLoading = false, generalError = message)
                }
                effectChannel.send(LoginEffect.ShowSnackbar(message))
            }
        }
    }
}
