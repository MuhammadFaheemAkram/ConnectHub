package com.example.connecthub.data.repository

import com.example.connecthub.core.common.dispatcher.DispatcherProvider
import com.example.connecthub.core.datastore.SettingsLocalDataSource
import com.example.connecthub.domain.model.AppSettings
import com.example.connecthub.domain.repository.SettingsRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class SettingsRepositoryImpl @Inject constructor(
    private val settingsLocalDataSource: SettingsLocalDataSource,
    private val dispatchers: DispatcherProvider,
) : SettingsRepository {
    override fun observeSettings(): Flow<AppSettings> = settingsLocalDataSource.settings

    override suspend fun updateTheme(darkMode: Boolean) {
        withContext(dispatchers.io) {
            settingsLocalDataSource.updateTheme(darkMode)
        }
    }

    override suspend fun updateDynamicColor(enabled: Boolean) {
        withContext(dispatchers.io) {
            settingsLocalDataSource.updateDynamicColor(enabled)
        }
    }

    override suspend fun updateNotificationSetting(enabled: Boolean) {
        withContext(dispatchers.io) {
            settingsLocalDataSource.updateNotificationSetting(enabled)
        }
    }

    override suspend fun updateLanguage(language: String) {
        withContext(dispatchers.io) {
            settingsLocalDataSource.updateLanguage(language)
        }
    }

    override suspend fun clearCache() {
        // Phase 6 keeps cache clearing non-destructive for portfolio demos; later phases can
        // swap this for a Room-backed cache manager without changing the ViewModel contract.
        withContext(dispatchers.io) {
            settingsLocalDataSource.recordCacheCleared()
        }
    }
}
