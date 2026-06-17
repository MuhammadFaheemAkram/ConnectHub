package com.example.connecthub.domain.usecase

import com.example.connecthub.domain.model.AuthSession
import com.example.connecthub.domain.repository.AuthRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(name: String, email: String, password: String): AuthSession {
        return authRepository.signUp(
            name = name.trim(),
            email = email.trim(),
            password = password,
        )
    }
}
