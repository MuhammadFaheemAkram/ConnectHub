package com.example.connecthub.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.connecthub.domain.usecase.EnsureProfileUseCase
import com.example.connecthub.domain.usecase.ObserveProfilePostsUseCase
import com.example.connecthub.domain.usecase.ObserveProfileUseCase
import com.example.connecthub.feature.feed.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val ensureProfileUseCase: EnsureProfileUseCase,
    private val observeProfileUseCase: ObserveProfileUseCase,
    private val observeProfilePostsUseCase: ObserveProfilePostsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            runCatching { ensureProfileUseCase() }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = throwable.message ?: "Unable to prepare profile",
                        )
                    }
                }
        }
        observeProfile()
        observePosts()
    }

    private fun observeProfile() {
        viewModelScope.launch {
            observeProfileUseCase()
                .catch { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = throwable.message ?: "Unable to load profile",
                        )
                    }
                }
                .collect { user ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            user = user,
                            errorMessage = null,
                        )
                    }
                }
        }
    }

    private fun observePosts() {
        viewModelScope.launch {
            observeProfilePostsUseCase()
                .catch { throwable ->
                    _uiState.update {
                        it.copy(errorMessage = throwable.message ?: "Unable to load profile posts")
                    }
                }
                .collect { posts ->
                    _uiState.update {
                        it.copy(posts = posts.map { post -> post.toUiModel() })
                    }
                }
        }
    }
}
