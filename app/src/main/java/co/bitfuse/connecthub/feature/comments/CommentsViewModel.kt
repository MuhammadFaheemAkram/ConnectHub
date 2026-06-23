package co.bitfuse.connecthub.feature.comments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.bitfuse.connecthub.domain.usecase.AddCommentUseCase
import co.bitfuse.connecthub.domain.usecase.DeleteCommentUseCase
import co.bitfuse.connecthub.domain.usecase.ObserveCommentsUseCase
import co.bitfuse.connecthub.domain.usecase.RefreshCommentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val observeCommentsUseCase: ObserveCommentsUseCase,
    private val refreshCommentsUseCase: RefreshCommentsUseCase,
    private val addCommentUseCase: AddCommentUseCase,
    private val deleteCommentUseCase: DeleteCommentUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CommentsUiState())
    val uiState: StateFlow<CommentsUiState> = _uiState.asStateFlow()

    private var currentPostId: String? = null
    private var commentsJob: Job? = null

    fun load(postId: String) {
        if (postId.isBlank() || currentPostId == postId) return
        currentPostId = postId
        commentsJob?.cancel()
        commentsJob = viewModelScope.launch {
            observeCommentsUseCase(postId)
                .catch { throwable ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = throwable.message ?: "Unable to read comments")
                    }
                }
                .collect { comments ->
                    _uiState.update {
                        it.copy(
                            comments = comments.map { comment -> comment.toUiModel() },
                            isLoading = false,
                        )
                    }
                }
        }
        refresh()
    }

    fun refresh() {
        val postId = currentPostId.orEmpty()
        if (postId.isBlank()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = it.comments.isEmpty(), errorMessage = null) }
            runCatching {
                refreshCommentsUseCase(postId)
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = throwable.message ?: "Unable to load comments",
                    )
                }
            }
        }
    }

    fun onDraftChange(text: String) {
        _uiState.update { it.copy(draft = text.take(280), errorMessage = null) }
    }

    fun addComment() {
        val postId = currentPostId.orEmpty()
        val text = _uiState.value.draft.trim()
        if (postId.isBlank() || text.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }
            runCatching {
                addCommentUseCase(postId = postId, text = text)
            }.onSuccess {
                _uiState.update { it.copy(draft = "", isSubmitting = false) }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(isSubmitting = false, errorMessage = throwable.message ?: "Unable to add comment")
                }
            }
        }
    }

    fun deleteComment(commentId: String) {
        viewModelScope.launch {
            runCatching {
                deleteCommentUseCase(commentId)
            }.onFailure { throwable ->
                _uiState.update { it.copy(errorMessage = throwable.message ?: "Unable to delete comment") }
            }
        }
    }
}
