package co.bitfuse.connecthub.feature.auth

data class SignUpUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val nameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val generalError: String? = null,
)

sealed interface SignUpEffect {
    data object NavigateToHome : SignUpEffect
    data class ShowSnackbar(val message: String) : SignUpEffect
}
