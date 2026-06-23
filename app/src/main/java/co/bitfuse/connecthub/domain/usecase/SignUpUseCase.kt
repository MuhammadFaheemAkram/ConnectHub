package co.bitfuse.connecthub.domain.usecase

import co.bitfuse.connecthub.domain.model.AuthSession
import co.bitfuse.connecthub.domain.repository.AuthRepository
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
