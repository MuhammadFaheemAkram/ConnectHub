package co.bitfuse.connecthub.domain.repository

import co.bitfuse.connecthub.domain.model.AuthSession
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun observeSession(): Flow<AuthSession?>
    suspend fun login(email: String, password: String): AuthSession
    suspend fun signUp(name: String, email: String, password: String): AuthSession
    suspend fun logout()
}
