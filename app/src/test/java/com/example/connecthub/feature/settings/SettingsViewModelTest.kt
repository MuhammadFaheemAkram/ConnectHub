package com.example.connecthub.feature.settings

import com.example.connecthub.core.testing.MainDispatcherRule
import com.example.connecthub.domain.usecase.ClearCacheUseCase
import com.example.connecthub.domain.usecase.ObserveSettingsUseCase
import com.example.connecthub.domain.usecase.UpdateDynamicColorUseCase
import com.example.connecthub.domain.usecase.UpdateLanguageUseCase
import com.example.connecthub.domain.usecase.UpdateNotificationSettingUseCase
import com.example.connecthub.domain.usecase.UpdateThemeUseCase
import com.example.connecthub.settings.FakeSettingsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `setting updates are reflected in state`() = runTest {
        val repository = FakeSettingsRepository()
        val viewModel = viewModel(repository)
        advanceUntilIdle()

        viewModel.updateTheme(true)
        viewModel.updateDynamicColor(true)
        viewModel.updateNotifications(false)
        viewModel.updateLanguage("French")
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.darkMode)
        assertTrue(viewModel.uiState.value.dynamicColor)
        assertFalse(viewModel.uiState.value.notificationsEnabled)
        assertEquals("French", viewModel.uiState.value.language)
    }

    @Test
    fun `clear cache emits snackbar effect`() = runTest {
        val repository = FakeSettingsRepository()
        val viewModel = viewModel(repository)
        advanceUntilIdle()

        viewModel.clearCache()
        advanceUntilIdle()

        assertTrue(repository.cacheCleared)
        assertTrue(viewModel.effects.first() is SettingsEffect.ShowSnackbar)
    }

    private fun viewModel(repository: FakeSettingsRepository): SettingsViewModel {
        return SettingsViewModel(
            observeSettingsUseCase = ObserveSettingsUseCase(repository),
            updateThemeUseCase = UpdateThemeUseCase(repository),
            updateDynamicColorUseCase = UpdateDynamicColorUseCase(repository),
            updateNotificationSettingUseCase = UpdateNotificationSettingUseCase(repository),
            updateLanguageUseCase = UpdateLanguageUseCase(repository),
            clearCacheUseCase = ClearCacheUseCase(repository),
        )
    }
}
