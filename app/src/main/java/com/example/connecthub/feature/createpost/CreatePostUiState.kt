package com.example.connecthub.feature.createpost

data class CreatePostUiState(
    val content: String = "",
    val imageUrl: String = "",
    val isSubmitting: Boolean = false,
    val contentError: String? = null,
    val imageUrlError: String? = null,
    val generalError: String? = null,
) {
    val remainingCharacters: Int = 280 - content.length
    val canSubmit: Boolean = content.isNotBlank() && content.length <= 280 && !isSubmitting
}

sealed interface CreatePostEffect {
    data object NavigateBack : CreatePostEffect
    data class ShowSnackbar(val message: String) : CreatePostEffect
}
