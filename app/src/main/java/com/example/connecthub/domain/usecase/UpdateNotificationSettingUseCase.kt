package com.example.connecthub.domain.usecase

import com.example.connecthub.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateNotificationSettingUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {
    suspend operator fun invoke(enabled: Boolean) {
        settingsRepository.updateNotificationSetting(enabled)
    }
}
