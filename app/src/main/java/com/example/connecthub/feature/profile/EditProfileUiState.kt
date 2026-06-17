package com.example.connecthub.feature.profile

data class EditProfileUiState(
    val name: String = "",
    val bio: String = "",
    val avatarUrl: String = "",
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val nameError: String? = null,
    val bioError: String? = null,
    val avatarUrlError: String? = null,
    val generalError: String? = null,
) {
    val canSave: Boolean
        get() = name.isNotBlank() &&
            bio.length <= maxBioLength &&
            avatarUrlError == null &&
            !isLoading &&
            !isSaving

    companion object {
        const val maxBioLength = 160
    }
}

sealed interface EditProfileEffect {
    data object NavigateBack : EditProfileEffect
    data class ShowSnackbar(val message: String) : EditProfileEffect
}
