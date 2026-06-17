package com.example.connecthub.domain.usecase

import com.example.connecthub.domain.model.AuthSession
import com.example.connecthub.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveSessionUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    operator fun invoke(): Flow<AuthSession?> = authRepository.observeSession()
}
