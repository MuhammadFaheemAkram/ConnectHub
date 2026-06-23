package co.bitfuse.connecthub.feature.createpost

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import co.bitfuse.connecthub.core.designsystem.component.ConnectHubButton
import co.bitfuse.connecthub.core.designsystem.component.ConnectHubTextField
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CreatePostScreen(
    onPostCreated: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreatePostViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                CreatePostEffect.NavigateBack -> onPostCreated()
                is CreatePostEffect.ShowSnackbar -> snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier,
    ) { innerPadding ->
        CreatePostContent(
            uiState = uiState,
            onContentChange = viewModel::onContentChange,
            onImageUrlChange = viewModel::onImageUrlChange,
            onSubmit = viewModel::submit,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
fun CreatePostContent(
    uiState: CreatePostUiState,
    onContentChange: (String) -> Unit,
    onImageUrlChange: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        OutlinedTextField(
            value = uiState.content,
            onValueChange = onContentChange,
            label = { Text(text = "What would you like to share?") },
            supportingText = uiState.contentError?.let { message ->
                { Text(text = message) }
            },
            isError = uiState.contentError != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
        )
        Text(
            text = "${uiState.content.length}/280",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        ConnectHubTextField(
            value = uiState.imageUrl,
            onValueChange = onImageUrlChange,
            label = "Image URL",
            leadingIcon = Icons.Outlined.Image,
            supportingText = uiState.imageUrlError,
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
            text = "Post",
            onClick = onSubmit,
            enabled = uiState.canSubmit,
            isLoading = uiState.isSubmitting,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
