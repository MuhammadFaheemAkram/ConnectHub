package co.bitfuse.connecthub.domain.usecase

import co.bitfuse.connecthub.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateThemeUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {
    suspend operator fun invoke(darkMode: Boolean) {
        settingsRepository.updateTheme(darkMode)
    }
}
