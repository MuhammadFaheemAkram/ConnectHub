package co.bitfuse.connecthub.feature.postdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.bitfuse.connecthub.domain.usecase.AddCommentUseCase
import co.bitfuse.connecthub.domain.usecase.BookmarkPostUseCase
import co.bitfuse.connecthub.domain.usecase.GetPostDetailsUseCase
import co.bitfuse.connecthub.domain.usecase.LikePostUseCase
import co.bitfuse.connecthub.domain.usecase.ObserveCommentsUseCase
import co.bitfuse.connecthub.domain.usecase.RefreshCommentsUseCase
import co.bitfuse.connecthub.domain.usecase.RefreshPostDetailsUseCase
import co.bitfuse.connecthub.feature.comments.toUiModel
import co.bitfuse.connecthub.feature.feed.toUiModel
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
class PostDetailViewModel @Inject constructor(
    private val getPostDetailsUseCase: GetPostDetailsUseCase,
    private val refreshPostDetailsUseCase: RefreshPostDetailsUseCase,
    private val observeCommentsUseCase: ObserveCommentsUseCase,
    private val refreshCommentsUseCase: RefreshCommentsUseCase,
    private val addCommentUseCase: AddCommentUseCase,
    private val likePostUseCase: LikePostUseCase,
    private val bookmarkPostUseCase: BookmarkPostUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PostDetailUiState())
    val uiState: StateFlow<PostDetailUiState> = _uiState.asStateFlow()

    private var currentPostId: String? = null
    private var postJob: Job? = null
    private var commentsJob: Job? = null

    fun load(postId: String) {
        if (postId.isBlank() || currentPostId == postId) return
        currentPostId = postId
        observePost(postId)
        observeComments(postId)
        refresh(postId)
    }

    fun refresh(postId: String = currentPostId.orEmpty()) {
        if (postId.isBlank()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = it.post == null, errorMessage = null) }
            runCatching {
                refreshPostDetailsUseCase(postId)
                refreshCommentsUseCase(postId)
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = throwable.message ?: "Unable to load post",
                    )
                }
            }
        }
    }

    fun onCommentDraftChange(text: String) {
        _uiState.update { it.copy(commentDraft = text.take(280), errorMessage = null) }
    }

    fun addComment() {
        val postId = currentPostId.orEmpty()
        val text = _uiState.value.commentDraft.trim()
        if (postId.isBlank() || text.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmittingComment = true, errorMessage = null) }
            runCatching {
                addCommentUseCase(postId = postId, text = text)
            }.onSuccess {
                _uiState.update { it.copy(commentDraft = "", isSubmittingComment = false) }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isSubmittingComment = false,
                        errorMessage = throwable.message ?: "Unable to add comment",
                    )
                }
            }
        }
    }

    fun likePost() {
        val postId = currentPostId.orEmpty()
        if (postId.isBlank()) return
        viewModelScope.launch {
            runCatching { likePostUseCase(postId) }
                .onFailure { throwable ->
                    _uiState.update { it.copy(errorMessage = throwable.message ?: "Unable to update like") }
                }
        }
    }

    fun bookmarkPost() {
        val postId = currentPostId.orEmpty()
        if (postId.isBlank()) return
        viewModelScope.launch {
            runCatching { bookmarkPostUseCase(postId) }
                .onFailure { throwable ->
                    _uiState.update { it.copy(errorMessage = throwable.message ?: "Unable to update bookmark") }
                }
        }
    }

    private fun observePost(postId: String) {
        postJob?.cancel()
        postJob = viewModelScope.launch {
            getPostDetailsUseCase(postId)
                .catch { throwable ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = throwable.message ?: "Unable to read post")
                    }
                }
                .collect { post ->
                    _uiState.update {
                        it.copy(
                            post = post?.toUiModel(),
                            isLoading = false,
                        )
                    }
                }
        }
    }

    private fun observeComments(postId: String) {
        commentsJob?.cancel()
        commentsJob = viewModelScope.launch {
            observeCommentsUseCase(postId)
                .catch { throwable ->
                    _uiState.update { it.copy(errorMessage = throwable.message ?: "Unable to read comments") }
                }
                .collect { comments ->
                    _uiState.update {
                        it.copy(commentsPreview = comments.take(3).map { comment -> comment.toUiModel() })
                    }
                }
        }
    }
}
