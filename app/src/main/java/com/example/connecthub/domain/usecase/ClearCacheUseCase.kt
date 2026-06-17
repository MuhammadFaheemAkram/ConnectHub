package com.example.connecthub.domain.usecase

import com.example.connecthub.domain.repository.SettingsRepository
import javax.inject.Inject

class ClearCacheUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {
    suspend operator fun invoke() {
        settingsRepository.clearCache()
    }
}
