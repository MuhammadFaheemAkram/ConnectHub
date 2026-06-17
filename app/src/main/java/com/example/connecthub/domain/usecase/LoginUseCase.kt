package com.example.connecthub.domain.usecase

import com.example.connecthub.domain.model.AuthSession
import com.example.connecthub.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(email: String, password: String): AuthSession {
        return authRepository.login(email = email.trim(), password = password)
    }
}
