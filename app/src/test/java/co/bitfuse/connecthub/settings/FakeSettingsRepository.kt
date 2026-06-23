package co.bitfuse.connecthub.settings

import co.bitfuse.connecthub.domain.model.AppSettings
import co.bitfuse.connecthub.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeSettingsRepository(
    initialSettings: AppSettings = AppSettings(),
) : SettingsRepository {
    private val settings = MutableStateFlow(initialSettings)
    var cacheCleared = false

    override fun observeSettings(): Flow<AppSettings> = settings

    override suspend fun updateTheme(darkMode: Boolean) {
        settings.value = settings.value.copy(darkMode = darkMode)
    }

    override suspend fun updateDynamicColor(enabled: Boolean) {
        settings.value = settings.value.copy(dynamicColor = enabled)
    }

    override suspend fun updateNotificationSetting(enabled: Boolean) {
        settings.value = settings.value.copy(notificationsEnabled = enabled)
    }

    override suspend fun updateLanguage(language: String) {
        settings.value = settings.value.copy(language = language)
    }

    override suspend fun clearCache() {
        cacheCleared = true
    }
}
