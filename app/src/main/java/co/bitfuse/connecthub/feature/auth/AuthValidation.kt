package co.bitfuse.connecthub.feature.auth

object AuthValidation {
    fun validateLogin(email: String, password: String): LoginValidationResult {
        val emailError = validateEmail(email)
        val passwordError = validatePassword(password)
        return LoginValidationResult(
            emailError = emailError,
            passwordError = passwordError,
            isValid = emailError == null && passwordError == null,
        )
    }

    fun validateSignUp(
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
    ): SignUpValidationResult {
        val nameError = when {
            name.isBlank() -> "Name is required"
            name.trim().length < 2 -> "Name must be at least 2 characters"
            else -> null
        }
        val emailError = validateEmail(email)
        val passwordError = validatePassword(password)
        val confirmPasswordError = when {
            confirmPassword.isBlank() -> "Confirm your password"
            password != confirmPassword -> "Passwords do not match"
            else -> null
        }

        return SignUpValidationResult(
            nameError = nameError,
            emailError = emailError,
            passwordError = passwordError,
            confirmPasswordError = confirmPasswordError,
            isValid = listOf(nameError, emailError, passwordError, confirmPasswordError).all { it == null },
        )
    }

    private fun validateEmail(email: String): String? = when {
        email.isBlank() -> "Email is required"
        !email.contains("@") || !email.substringAfter("@").contains(".") -> "Enter a valid email"
        else -> null
    }

    private fun validatePassword(password: String): String? = when {
        password.isBlank() -> "Password is required"
        password.length < 6 -> "Password must be at least 6 characters"
        else -> null
    }
}

data class LoginValidationResult(
    val emailError: String?,
    val passwordError: String?,
    val isValid: Boolean,
)

data class SignUpValidationResult(
    val nameError: String?,
    val emailError: String?,
    val passwordError: String?,
    val confirmPasswordError: String?,
    val isValid: Boolean,
)
