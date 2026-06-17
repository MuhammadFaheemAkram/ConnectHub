package com.example.connecthub.feature.bookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.connecthub.domain.usecase.ObserveBookmarksUseCase
import com.example.connecthub.domain.usecase.RemoveBookmarkUseCase
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
class BookmarksViewModel @Inject constructor(
    private val observeBookmarksUseCase: ObserveBookmarksUseCase,
    private val removeBookmarkUseCase: RemoveBookmarkUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(BookmarksUiState())
    val uiState: StateFlow<BookmarksUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeBookmarksUseCase()
                .catch { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = throwable.message ?: "Unable to load bookmarks",
                        )
                    }
                }
                .collect { posts ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            posts = posts.map { post -> post.toUiModel() },
                            errorMessage = null,
                        )
                    }
                }
        }
    }

    fun removeBookmark(postId: String) {
        viewModelScope.launch {
            runCatching { removeBookmarkUseCase(postId) }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(errorMessage = throwable.message ?: "Unable to remove bookmark")
                    }
                }
        }
    }
}
