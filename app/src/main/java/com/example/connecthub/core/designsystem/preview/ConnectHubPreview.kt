package com.example.connecthub.core.designsystem.preview

import androidx.compose.runtime.Composable
import com.example.connecthub.core.designsystem.theme.ConnectHubTheme

@Composable
fun ConnectHubPreview(content: @Composable () -> Unit) {
    ConnectHubTheme(dynamicColor = false, content = content)
}
