package com.example.connecthub.core.designsystem.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ConnectHubErrorState(
    title: String,
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ConnectHubEmptyState(
        title = title,
        message = message,
        modifier = modifier,
        icon = Icons.Outlined.ErrorOutline,
        action = {
            ConnectHubOutlinedButton(
                text = "Retry",
                onClick = onRetry,
            )
        },
    )
}
