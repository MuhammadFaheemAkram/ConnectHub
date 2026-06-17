package com.example.connecthub.domain.repository

import com.example.connecthub.domain.model.AppSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun observeSettings(): Flow<AppSettings>
    suspend fun updateTheme(darkMode: Boolean)
    suspend fun updateDynamicColor(enabled: Boolean)
    suspend fun updateNotificationSetting(enabled: Boolean)
    suspend fun updateLanguage(language: String)
    suspend fun clearCache()
}
