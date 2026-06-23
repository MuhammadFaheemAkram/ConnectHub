package co.bitfuse.connecthub.domain.usecase

import co.bitfuse.connecthub.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateLanguageUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {
    suspend operator fun invoke(language: String) {
        settingsRepository.updateLanguage(language)
    }
}
