package co.bitfuse.connecthub.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.bitfuse.connecthub.domain.usecase.EnsureProfileUseCase
import co.bitfuse.connecthub.domain.usecase.ObserveProfileUseCase
import co.bitfuse.connecthub.domain.usecase.UpdateProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val ensureProfileUseCase: EnsureProfileUseCase,
    private val observeProfileUseCase: ObserveProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    private val effectChannel = Channel<EditProfileEffect>(Channel.BUFFERED)
    val effects = effectChannel.receiveAsFlow()

    private var loadedInitialProfile = false

    init {
        viewModelScope.launch {
            runCatching { ensureProfileUseCase() }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            generalError = throwable.message ?: "Unable to prepare profile",
                        )
                    }
                }
        }
        viewModelScope.launch {
            observeProfileUseCase()
                .catch { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            generalError = throwable.message ?: "Unable to load profile",
                        )
                    }
                }
                .collect { user ->
                    if (!loadedInitialProfile) {
                        loadedInitialProfile = true
                        _uiState.update {
                            it.copy(
                                name = user.name,
                                bio = user.bio,
                                avatarUrl = user.avatarUrl,
                                isLoading = false,
                                generalError = null,
                            )
                        }
                    }
                }
        }
    }

    fun onNameChange(value: String) {
        _uiState.update {
            it.copy(
                name = value,
                nameError = null,
                generalError = null,
            )
        }
    }

    fun onBioChange(value: String) {
        _uiState.update {
            it.copy(
                bio = value,
                bioError = null,
                generalError = null,
            )
        }
    }

    fun onAvatarUrlChange(value: String) {
        _uiState.update {
            it.copy(
                avatarUrl = value,
                avatarUrlError = null,
                generalError = null,
            )
        }
    }

    fun saveProfile() {
        val current = uiState.value
        val nameError = if (current.name.isBlank()) "Name is required" else null
        val bioError = if (current.bio.length > EditProfileUiState.maxBioLength) {
            "Bio must be ${EditProfileUiState.maxBioLength} characters or fewer"
        } else {
            null
        }
        val avatarUrlError = if (
            current.avatarUrl.isNotBlank() &&
            !current.avatarUrl.startsWith("https://") &&
            !current.avatarUrl.startsWith("http://")
        ) {
            "Use a valid image URL"
        } else {
            null
        }

        if (nameError != null || bioError != null || avatarUrlError != null) {
            _uiState.update {
                it.copy(
                    nameError = nameError,
                    bioError = bioError,
                    avatarUrlError = avatarUrlError,
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, generalError = null) }
            runCatching {
                updateProfileUseCase(
                    name = current.name,
                    bio = current.bio,
                    avatarUrl = current.avatarUrl,
                )
            }.onSuccess {
                _uiState.update { it.copy(isSaving = false) }
                effectChannel.send(EditProfileEffect.NavigateBack)
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        generalError = throwable.message ?: "Unable to save profile",
                    )
                }
                effectChannel.send(EditProfileEffect.ShowSnackbar("Profile was not saved"))
            }
        }
    }
}
