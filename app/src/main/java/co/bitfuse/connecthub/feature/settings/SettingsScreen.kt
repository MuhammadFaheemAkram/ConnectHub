package co.bitfuse.connecthub.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.bitfuse.connecthub.core.designsystem.component.ConnectHubLoadingState
import co.bitfuse.connecthub.core.designsystem.component.FeaturePlaceholderScreen
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SettingsRoute(
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                is SettingsEffect.ShowSnackbar -> snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier,
    ) { innerPadding ->
        SettingsScreen(
            uiState = uiState,
            onDarkModeChange = viewModel::updateTheme,
            onDynamicColorChange = viewModel::updateDynamicColor,
            onNotificationsChange = viewModel::updateNotifications,
            onLanguageChange = viewModel::updateLanguage,
            onClearCache = viewModel::clearCache,
            onLogout = onLogout,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    onDarkModeChange: (Boolean) -> Unit,
    onDynamicColorChange: (Boolean) -> Unit,
    onNotificationsChange: (Boolean) -> Unit,
    onLanguageChange: (String) -> Unit,
    onClearCache: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var languageExpanded by remember { mutableStateOf(false) }

    if (uiState.isLoading) {
        ConnectHubLoadingState(
            message = "Loading settings",
            modifier = modifier.fillMaxSize(),
        )
        return
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        item {
            ListItem(
                headlineContent = { Text(text = "Dark mode") },
                leadingContent = { Icon(Icons.Outlined.DarkMode, contentDescription = null) },
                trailingContent = {
                    Switch(
                        checked = uiState.darkMode,
                        onCheckedChange = onDarkModeChange,
                    )
                },
            )
        }
        item {
            ListItem(
                headlineContent = { Text(text = "Dynamic color") },
                leadingContent = { Icon(Icons.Outlined.AutoAwesome, contentDescription = null) },
                trailingContent = {
                    Switch(
                        checked = uiState.dynamicColor,
                        onCheckedChange = onDynamicColorChange,
                    )
                },
            )
        }
        item {
            ListItem(
                headlineContent = { Text(text = "Notifications") },
                leadingContent = { Icon(Icons.Outlined.NotificationsNone, contentDescription = null) },
                trailingContent = {
                    Switch(
                        checked = uiState.notificationsEnabled,
                        onCheckedChange = onNotificationsChange,
                    )
                },
            )
        }
        item {
            ListItem(
                headlineContent = { Text(text = "Language") },
                supportingContent = { Text(text = uiState.language) },
                leadingContent = { Icon(Icons.Outlined.Language, contentDescription = null) },
                modifier = Modifier.clickable { languageExpanded = true },
            )
            DropdownMenu(
                expanded = languageExpanded,
                onDismissRequest = { languageExpanded = false },
            ) {
                languages.forEach { language ->
                    DropdownMenuItem(
                        text = { Text(text = language) },
                        onClick = {
                            languageExpanded = false
                            onLanguageChange(language)
                        },
                    )
                }
            }
        }
        item {
            ListItem(
                headlineContent = { Text(text = if (uiState.isClearingCache) "Clearing cache" else "Clear cache") },
                leadingContent = { Icon(Icons.Outlined.DeleteSweep, contentDescription = null) },
                modifier = Modifier.clickable(enabled = !uiState.isClearingCache, onClick = onClearCache),
            )
        }
        item {
            ListItem(
                headlineContent = { Text(text = "Logout") },
                leadingContent = { Icon(Icons.AutoMirrored.Outlined.Logout, contentDescription = null) },
                modifier = Modifier.clickable(onClick = onLogout),
            )
        }
        uiState.errorMessage?.let { message ->
            item {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                )
            }
        }
    }
}

@Composable
fun AboutScreen(
    modifier: Modifier = Modifier,
) {
    FeaturePlaceholderScreen(
        title = "ConnectHub",
        message = "A modern open-source Android social feed app built with Kotlin and Jetpack Compose.",
        icon = Icons.Outlined.Info,
        modifier = modifier,
    )
}

private val languages = listOf("English", "Spanish", "French")
