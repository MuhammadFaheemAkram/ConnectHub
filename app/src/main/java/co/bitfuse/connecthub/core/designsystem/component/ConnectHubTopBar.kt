package co.bitfuse.connecthub.core.designsystem.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectHubTopBar(
    title: String,
    navigationType: ConnectHubTopBarNavigationType,
    onNavigationClick: () -> Unit,
    actions: @Composable () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            val icon = navigationType.icon
            if (icon != null) {
                IconButton(onClick = onNavigationClick) {
                    Icon(
                        imageVector = icon,
                        contentDescription = navigationType.contentDescription,
                    )
                }
            }
        },
        actions = { actions() },
        colors = TopAppBarDefaults.topAppBarColors(),
    )
}

enum class ConnectHubTopBarNavigationType(
    val icon: ImageVector?,
    val contentDescription: String?,
) {
    None(icon = null, contentDescription = null),
    Drawer(icon = Icons.Outlined.Menu, contentDescription = "Open navigation drawer"),
    Back(icon = Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Navigate back"),
}
