package com.example.connecthub.feature.settings

data class SettingsUiState(
    val isLoading: Boolean = true,
    val darkMode: Boolean = false,
    val dynamicColor: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val language: String = "English",
    val isClearingCache: Boolean = false,
    val errorMessage: String? = null,
)

sealed interface SettingsEffect {
    data class ShowSnackbar(val message: String) : SettingsEffect
}
