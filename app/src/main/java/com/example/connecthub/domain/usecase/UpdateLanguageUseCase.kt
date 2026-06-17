package com.example.connecthub.domain.usecase

import com.example.connecthub.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateLanguageUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {
    suspend operator fun invoke(language: String) {
        settingsRepository.updateLanguage(language)
    }
}
