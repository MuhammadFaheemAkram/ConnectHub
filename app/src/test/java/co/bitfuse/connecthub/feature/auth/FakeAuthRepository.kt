package co.bitfuse.connecthub.feature.auth

import co.bitfuse.connecthub.domain.model.AuthSession
import co.bitfuse.connecthub.domain.model.User
import co.bitfuse.connecthub.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeAuthRepository(
    initialSession: AuthSession? = null,
) : AuthRepository {
    private val sessionFlow = MutableStateFlow(initialSession)
    var shouldThrow = false
    var lastLoginEmail: String? = null
    var lastSignUpName: String? = null

    override fun observeSession(): Flow<AuthSession?> = sessionFlow

    override suspend fun login(email: String, password: String): AuthSession {
        if (shouldThrow) error("Auth failed")
        lastLoginEmail = email
        return sampleSession(email = email, name = "Test User").also {
            sessionFlow.value = it
        }
    }

    override suspend fun signUp(name: String, email: String, password: String): AuthSession {
        if (shouldThrow) error("Sign up failed")
        lastSignUpName = name
        return sampleSession(email = email, name = name).also {
            sessionFlow.value = it
        }
    }

    override suspend fun logout() {
        sessionFlow.value = null
    }

    fun setSession(session: AuthSession?) {
        sessionFlow.value = session
    }

    companion object {
        fun sampleSession(
            email: String = "user@connecthub.dev",
            name: String = "Test User",
        ): AuthSession {
            val user = User(
                id = "user-${email.hashCode()}",
                name = name,
                avatarUrl = "",
                bio = "Testing ConnectHub",
                followersCount = 1,
                followingCount = 2,
            )
            return AuthSession(
                token = "token-${email.hashCode()}",
                userId = user.id,
                user = user,
            )
        }
    }
}
