package com.example.connecthub.feature

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.connecthub.core.designsystem.theme.ConnectHubTheme
import com.example.connecthub.feature.chatdetail.ChatDetailScreen
import com.example.connecthub.feature.chatdetail.ChatDetailUiState
import com.example.connecthub.feature.chatdetail.MessageUiModel
import com.example.connecthub.feature.notifications.NotificationFilter
import com.example.connecthub.feature.notifications.NotificationsScreen
import com.example.connecthub.feature.notifications.NotificationsUiState
import com.example.connecthub.feature.settings.SettingsScreen
import com.example.connecthub.feature.settings.SettingsUiState
import org.junit.Rule
import org.junit.Test

class Phase6ComposeTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun settingsScreenShowsDarkModeToggle() {
        composeRule.setContent {
            ConnectHubTheme {
                SettingsScreen(
                    uiState = SettingsUiState(isLoading = false),
                    onDarkModeChange = {},
                    onDynamicColorChange = {},
                    onNotificationsChange = {},
                    onLanguageChange = {},
                    onClearCache = {},
                    onLogout = {},
                )
            }
        }

        composeRule.onNodeWithText("Dark mode").assertIsDisplayed()
        composeRule.onNodeWithText("Dynamic color").assertIsDisplayed()
    }

    @Test
    fun chatDetailDisplaysMessageBubbles() {
        composeRule.setContent {
            ConnectHubTheme {
                ChatDetailScreen(
                    uiState = ChatDetailUiState(
                        isLoading = false,
                        messages = listOf(
                            MessageUiModel(
                                id = "message-1",
                                text = "Can you review this?",
                                isMine = false,
                            ),
                        ),
                    ),
                    onDraftChange = {},
                    onSend = {},
                )
            }
        }

        composeRule.onNodeWithText("Can you review this?").assertIsDisplayed()
        composeRule.onNodeWithText("Message").assertIsDisplayed()
    }

    @Test
    fun notificationsScreenShowsFilters() {
        composeRule.setContent {
            ConnectHubTheme {
                NotificationsScreen(
                    uiState = NotificationsUiState(
                        isLoading = false,
                        selectedFilter = NotificationFilter.All,
                    ),
                    onSelectFilter = {},
                    onMarkRead = {},
                    onMarkAllRead = {},
                    onRetry = {},
                    onOpenPost = {},
                )
            }
        }

        composeRule.onNodeWithText("All").assertIsDisplayed()
        composeRule.onNodeWithText("Unread").assertIsDisplayed()
    }
}
