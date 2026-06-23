package co.bitfuse.connecthub.domain.usecase

import co.bitfuse.connecthub.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateDynamicColorUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {
    suspend operator fun invoke(enabled: Boolean) {
        settingsRepository.updateDynamicColor(enabled)
    }
}
