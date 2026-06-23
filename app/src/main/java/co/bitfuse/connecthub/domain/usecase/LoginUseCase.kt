package co.bitfuse.connecthub.domain.usecase

import co.bitfuse.connecthub.domain.model.AuthSession
import co.bitfuse.connecthub.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(email: String, password: String): AuthSession {
        return authRepository.login(email = email.trim(), password = password)
    }
}
