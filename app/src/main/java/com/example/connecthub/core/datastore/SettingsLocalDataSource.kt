package com.example.connecthub.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.connecthub.domain.model.AppSettings
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class SettingsLocalDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    val settings: Flow<AppSettings> = dataStore.data.map { preferences ->
        AppSettings(
            darkMode = preferences[Keys.darkMode] ?: false,
            dynamicColor = preferences[Keys.dynamicColor] ?: false,
            notificationsEnabled = preferences[Keys.notificationsEnabled] ?: true,
            language = preferences[Keys.language] ?: "English",
        )
    }

    suspend fun updateTheme(darkMode: Boolean) {
        dataStore.edit { preferences -> preferences[Keys.darkMode] = darkMode }
    }

    suspend fun updateDynamicColor(enabled: Boolean) {
        dataStore.edit { preferences -> preferences[Keys.dynamicColor] = enabled }
    }

    suspend fun updateNotificationSetting(enabled: Boolean) {
        dataStore.edit { preferences -> preferences[Keys.notificationsEnabled] = enabled }
    }

    suspend fun updateLanguage(language: String) {
        dataStore.edit { preferences -> preferences[Keys.language] = language }
    }

    suspend fun recordCacheCleared() {
        dataStore.edit { preferences -> preferences[Keys.lastCacheClearedAt] = System.currentTimeMillis() }
    }

    private object Keys {
        val darkMode = booleanPreferencesKey("dark_mode")
        val dynamicColor = booleanPreferencesKey("dynamic_color")
        val notificationsEnabled = booleanPreferencesKey("notifications_enabled")
        val language = stringPreferencesKey("language")
        val lastCacheClearedAt = longPreferencesKey("last_cache_cleared_at")
    }
}
