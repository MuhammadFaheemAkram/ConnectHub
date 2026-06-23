package co.bitfuse.connecthub.feature.auth

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
    val generalError: String? = null,
)

sealed interface LoginEffect {
    data object NavigateToHome : LoginEffect
    data class ShowSnackbar(val message: String) : LoginEffect
}
