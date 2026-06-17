package com.example.connecthub.feature.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.connecthub.core.designsystem.component.ConnectHubButton
import com.example.connecthub.core.designsystem.component.ConnectHubLoadingState
import com.example.connecthub.core.designsystem.component.ConnectHubTextField
import kotlinx.coroutines.flow.collectLatest

@Composable
fun EditProfileRoute(
    onSaved: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditProfileViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                EditProfileEffect.NavigateBack -> onSaved()
                is EditProfileEffect.ShowSnackbar -> snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier,
    ) { innerPadding ->
        EditProfileScreen(
            uiState = uiState,
            onNameChange = viewModel::onNameChange,
            onBioChange = viewModel::onBioChange,
            onAvatarUrlChange = viewModel::onAvatarUrlChange,
            onSave = viewModel::saveProfile,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
fun EditProfileScreen(
    uiState: EditProfileUiState,
    onNameChange: (String) -> Unit,
    onBioChange: (String) -> Unit,
    onAvatarUrlChange: (String) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (uiState.isLoading) {
        ConnectHubLoadingState(
            message = "Loading profile",
            modifier = modifier.fillMaxSize(),
        )
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        ConnectHubTextField(
            value = uiState.name,
            onValueChange = onNameChange,
            label = "Name",
            leadingIcon = Icons.Outlined.Person,
            supportingText = uiState.nameError,
            modifier = Modifier.fillMaxWidth(),
        )
        ConnectHubTextField(
            value = uiState.bio,
            onValueChange = onBioChange,
            label = "Bio",
            supportingText = uiState.bioError ?: "${uiState.bio.length}/${EditProfileUiState.maxBioLength}",
            isError = uiState.bioError != null,
            modifier = Modifier.fillMaxWidth(),
            singleLine = false,
        )
        ConnectHubTextField(
            value = uiState.avatarUrl,
            onValueChange = onAvatarUrlChange,
            label = "Avatar URL",
            leadingIcon = Icons.Outlined.Image,
            supportingText = uiState.avatarUrlError,
            modifier = Modifier.fillMaxWidth(),
        )
        uiState.generalError?.let { message ->
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        ConnectHubButton(
            text = "Save profile",
            onClick = onSave,
            enabled = uiState.canSave,
            isLoading = uiState.isSaving,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
