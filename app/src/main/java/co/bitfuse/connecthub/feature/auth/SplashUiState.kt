package co.bitfuse.connecthub.feature.auth

sealed interface SplashUiState {
    data object Loading : SplashUiState
    data object Authenticated : SplashUiState
    data object Unauthenticated : SplashUiState
}
