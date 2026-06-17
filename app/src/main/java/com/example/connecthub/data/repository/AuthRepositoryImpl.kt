package com.example.connecthub.data.repository

import com.example.connecthub.core.common.dispatcher.DispatcherProvider
import com.example.connecthub.core.datastore.SessionLocalDataSource
import com.example.connecthub.data.auth.AuthApiService
import com.example.connecthub.data.mapper.toDomain
import com.example.connecthub.domain.model.AuthSession
import com.example.connecthub.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService,
    private val sessionLocalDataSource: SessionLocalDataSource,
    private val dispatchers: DispatcherProvider,
) : AuthRepository {
    override fun observeSession(): Flow<AuthSession?> = sessionLocalDataSource.session

    override suspend fun login(email: String, password: String): AuthSession = withContext(dispatchers.io) {
        authApiService.login(email = email, password = password)
            .toDomain()
            .also { sessionLocalDataSource.saveSession(it) }
    }

    override suspend fun signUp(name: String, email: String, password: String): AuthSession = withContext(dispatchers.io) {
        authApiService.signUp(name = name, email = email, password = password)
            .toDomain()
            .also { sessionLocalDataSource.saveSession(it) }
    }

    override suspend fun logout() {
        withContext(dispatchers.io) {
            sessionLocalDataSource.clearSession()
        }
    }
}
