package co.bitfuse.connecthub.feature.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.bitfuse.connecthub.domain.usecase.BookmarkPostUseCase
import co.bitfuse.connecthub.domain.usecase.LikePostUseCase
import co.bitfuse.connecthub.domain.usecase.ObserveFeedUseCase
import co.bitfuse.connecthub.domain.usecase.RefreshFeedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val observeFeedUseCase: ObserveFeedUseCase,
    private val refreshFeedUseCase: RefreshFeedUseCase,
    private val likePostUseCase: LikePostUseCase,
    private val bookmarkPostUseCase: BookmarkPostUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(FeedUiState(isLoading = true))
    val uiState: StateFlow<FeedUiState> = _uiState.asStateFlow()

    private var nextPage = 1
    private var hasCompletedInitialLoad = false

    init {
        observeCachedFeed()
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val hasPosts = _uiState.value.posts.isNotEmpty()
            _uiState.update {
                it.copy(
                    isLoading = !hasPosts,
                    isRefreshing = hasPosts,
                    errorMessage = null,
                )
            }

            runCatching {
                refreshFeedUseCase(page = 1)
            }.onSuccess { hasMore ->
                hasCompletedInitialLoad = true
                nextPage = 2
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false,
                        canLoadMore = hasMore,
                    )
                }
            }.onFailure { throwable ->
                hasCompletedInitialLoad = true
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false,
                        errorMessage = throwable.message ?: "Unable to refresh feed",
                    )
                }
            }
        }
    }

    fun loadNextPage() {
        val state = _uiState.value
        if (state.isLoadingMore || !state.canLoadMore) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMore = true, errorMessage = null) }
            runCatching {
                refreshFeedUseCase(page = nextPage)
            }.onSuccess { hasMore ->
                if (hasMore) nextPage += 1
                _uiState.update {
                    it.copy(
                        isLoadingMore = false,
                        canLoadMore = hasMore,
                    )
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isLoadingMore = false,
                        errorMessage = throwable.message ?: "Unable to load more posts",
                    )
                }
            }
        }
    }

    fun likePost(postId: String) {
        viewModelScope.launch {
            runCatching {
                likePostUseCase(postId)
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(errorMessage = throwable.message ?: "Unable to update like")
                }
            }
        }
    }

    fun bookmarkPost(postId: String) {
        viewModelScope.launch {
            runCatching {
                bookmarkPostUseCase(postId)
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(errorMessage = throwable.message ?: "Unable to update bookmark")
                }
            }
        }
    }

    private fun observeCachedFeed() {
        viewModelScope.launch {
            observeFeedUseCase()
                .catch { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = throwable.message ?: "Unable to read cached feed",
                        )
                    }
                }
                .collect { posts ->
                    _uiState.update { state ->
                        state.copy(
                            posts = posts.map { it.toUiModel() },
                            isLoading = state.isLoading && posts.isEmpty() && !hasCompletedInitialLoad,
                        )
                    }
                }
        }
    }
}
