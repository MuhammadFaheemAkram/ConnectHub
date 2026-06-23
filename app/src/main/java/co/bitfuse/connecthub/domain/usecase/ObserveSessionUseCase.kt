package co.bitfuse.connecthub.domain.usecase

import co.bitfuse.connecthub.domain.model.AuthSession
import co.bitfuse.connecthub.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveSessionUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    operator fun invoke(): Flow<AuthSession?> = authRepository.observeSession()
}
