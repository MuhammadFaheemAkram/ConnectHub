package co.bitfuse.connecthub.feature.auth

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthValidationTest {
    @Test
    fun `login validation rejects blank email and short password`() {
        val result = AuthValidation.validateLogin(
            email = "",
            password = "123",
        )

        assertFalse(result.isValid)
        assertEquals("Email is required", result.emailError)
        assertEquals("Password must be at least 6 characters", result.passwordError)
    }

    @Test
    fun `login validation accepts valid credentials`() {
        val result = AuthValidation.validateLogin(
            email = "alex@connecthub.dev",
            password = "password",
        )

        assertTrue(result.isValid)
        assertNull(result.emailError)
        assertNull(result.passwordError)
    }

    @Test
    fun `sign up validation rejects mismatched passwords`() {
        val result = AuthValidation.validateSignUp(
            name = "Alex",
            email = "alex@connecthub.dev",
            password = "password",
            confirmPassword = "different",
        )

        assertFalse(result.isValid)
        assertEquals("Passwords do not match", result.confirmPasswordError)
    }

    @Test
    fun `sign up validation accepts valid form`() {
        val result = AuthValidation.validateSignUp(
            name = "Alex",
            email = "alex@connecthub.dev",
            password = "password",
            confirmPassword = "password",
        )

        assertTrue(result.isValid)
        assertNull(result.nameError)
        assertNull(result.emailError)
        assertNull(result.passwordError)
        assertNull(result.confirmPasswordError)
    }
}
