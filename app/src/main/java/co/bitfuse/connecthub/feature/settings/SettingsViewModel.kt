package co.bitfuse.connecthub.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.bitfuse.connecthub.domain.usecase.ClearCacheUseCase
import co.bitfuse.connecthub.domain.usecase.ObserveSettingsUseCase
import co.bitfuse.connecthub.domain.usecase.UpdateDynamicColorUseCase
import co.bitfuse.connecthub.domain.usecase.UpdateLanguageUseCase
import co.bitfuse.connecthub.domain.usecase.UpdateNotificationSettingUseCase
import co.bitfuse.connecthub.domain.usecase.UpdateThemeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val observeSettingsUseCase: ObserveSettingsUseCase,
    private val updateThemeUseCase: UpdateThemeUseCase,
    private val updateDynamicColorUseCase: UpdateDynamicColorUseCase,
    private val updateNotificationSettingUseCase: UpdateNotificationSettingUseCase,
    private val updateLanguageUseCase: UpdateLanguageUseCase,
    private val clearCacheUseCase: ClearCacheUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private val effectChannel = Channel<SettingsEffect>(Channel.BUFFERED)
    val effects = effectChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            observeSettingsUseCase()
                .catch { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = throwable.message ?: "Unable to load settings",
                        )
                    }
                }
                .collect { settings ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            darkMode = settings.darkMode,
                            dynamicColor = settings.dynamicColor,
                            notificationsEnabled = settings.notificationsEnabled,
                            language = settings.language,
                            errorMessage = null,
                        )
                    }
                }
        }
    }

    fun updateTheme(enabled: Boolean) {
        launchSettingUpdate { updateThemeUseCase(enabled) }
    }

    fun updateDynamicColor(enabled: Boolean) {
        launchSettingUpdate { updateDynamicColorUseCase(enabled) }
    }

    fun updateNotifications(enabled: Boolean) {
        launchSettingUpdate { updateNotificationSettingUseCase(enabled) }
    }

    fun updateLanguage(language: String) {
        launchSettingUpdate { updateLanguageUseCase(language) }
    }

    fun clearCache() {
        viewModelScope.launch {
            _uiState.update { it.copy(isClearingCache = true, errorMessage = null) }
            runCatching { clearCacheUseCase() }
                .onSuccess {
                    _uiState.update { it.copy(isClearingCache = false) }
                    effectChannel.send(SettingsEffect.ShowSnackbar("Cache cleared"))
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isClearingCache = false,
                            errorMessage = throwable.message ?: "Unable to clear cache",
                        )
                    }
                }
        }
    }

    private fun launchSettingUpdate(block: suspend () -> Unit) {
        viewModelScope.launch {
            runCatching { block() }
                .onFailure { throwable ->
                    _uiState.update { it.copy(errorMessage = throwable.message ?: "Unable to update settings") }
                }
        }
    }
}
