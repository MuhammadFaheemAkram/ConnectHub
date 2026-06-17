package com.example.connecthub.feature.createpost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.connecthub.domain.usecase.CreatePostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val createPostUseCase: CreatePostUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreatePostUiState())
    val uiState: StateFlow<CreatePostUiState> = _uiState.asStateFlow()

    private val effectChannel = Channel<CreatePostEffect>(Channel.BUFFERED)
    val effects = effectChannel.receiveAsFlow()

    fun onContentChange(content: String) {
        _uiState.update {
            it.copy(
                content = content.take(280),
                contentError = null,
                generalError = null,
            )
        }
    }

    fun onImageUrlChange(imageUrl: String) {
        _uiState.update {
            it.copy(
                imageUrl = imageUrl,
                imageUrlError = null,
                generalError = null,
            )
        }
    }

    fun submit() {
        val state = _uiState.value
        val contentError = when {
            state.content.isBlank() -> "Post text is required"
            state.content.length > 280 -> "Post must be 280 characters or less"
            else -> null
        }
        val imageUrlError = when {
            state.imageUrl.isBlank() -> null
            state.imageUrl.startsWith("http://") || state.imageUrl.startsWith("https://") -> null
            else -> "Use a valid image URL"
        }
        if (contentError != null || imageUrlError != null) {
            _uiState.update {
                it.copy(
                    contentError = contentError,
                    imageUrlError = imageUrlError,
                    generalError = null,
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, generalError = null) }
            runCatching {
                createPostUseCase(
                    content = state.content,
                    imageUrl = state.imageUrl,
                )
            }.onSuccess {
                _uiState.update { CreatePostUiState() }
                effectChannel.send(CreatePostEffect.NavigateBack)
            }.onFailure { throwable ->
                val message = throwable.message ?: "Unable to create post"
                _uiState.update {
                    it.copy(isSubmitting = false, generalError = message)
                }
                effectChannel.send(CreatePostEffect.ShowSnackbar(message))
            }
        }
    }
}
