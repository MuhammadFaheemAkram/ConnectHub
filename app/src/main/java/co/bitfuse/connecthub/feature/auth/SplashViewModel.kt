package co.bitfuse.connecthub.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.bitfuse.connecthub.domain.usecase.ObserveSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

@HiltViewModel
class SplashViewModel @Inject constructor(
    observeSessionUseCase: ObserveSessionUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Loading)
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeSessionUseCase()
                .catch { _uiState.value = SplashUiState.Unauthenticated }
                .collect { session ->
                    _uiState.value = if (session == null) {
                        SplashUiState.Unauthenticated
                    } else {
                        SplashUiState.Authenticated
                    }
                }
        }
    }
}
