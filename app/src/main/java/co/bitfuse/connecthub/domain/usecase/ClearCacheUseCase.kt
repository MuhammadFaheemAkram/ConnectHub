package co.bitfuse.connecthub.domain.usecase

import co.bitfuse.connecthub.domain.repository.SettingsRepository
import javax.inject.Inject

class ClearCacheUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {
    suspend operator fun invoke() {
        settingsRepository.clearCache()
    }
}
