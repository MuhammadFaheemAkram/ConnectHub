package co.bitfuse.connecthub.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.ui.graphics.vector.ImageVector
import co.bitfuse.connecthub.core.designsystem.component.ConnectHubNavigationItem

val ConnectHubBottomDestinations = listOf(
    ConnectHubNavigationItem(
        route = MainRoute.Feed.route,
        label = MainRoute.Feed.title,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
    ),
    ConnectHubNavigationItem(
        route = MainRoute.Search.route,
        label = MainRoute.Search.title,
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Outlined.Search,
    ),
    ConnectHubNavigationItem(
        route = MainRoute.Bookmarks.route,
        label = MainRoute.Bookmarks.title,
        selectedIcon = Icons.Filled.Star,
        unselectedIcon = Icons.Outlined.StarBorder,
    ),
    ConnectHubNavigationItem(
        route = MainRoute.Notifications.route,
        label = MainRoute.Notifications.title,
        selectedIcon = Icons.Filled.Notifications,
        unselectedIcon = Icons.Outlined.NotificationsNone,
        badgeCount = 3,
    ),
    ConnectHubNavigationItem(
        route = MainRoute.Profile.route,
        label = MainRoute.Profile.title,
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
    ),
)

data class DrawerDestination(
    val route: String?,
    val label: String,
    val icon: ImageVector,
)

val ConnectHubDrawerDestinations = listOf(
    DrawerDestination(
        route = MainRoute.Feed.route,
        label = MainRoute.Feed.title,
        icon = Icons.Outlined.Home,
    ),
    DrawerDestination(
        route = MainRoute.ChatList.route,
        label = MainRoute.ChatList.title,
        icon = Icons.Outlined.ChatBubbleOutline,
    ),
    DrawerDestination(
        route = MainRoute.Settings.route,
        label = MainRoute.Settings.title,
        icon = Icons.Outlined.Settings,
    ),
    DrawerDestination(
        route = MainRoute.About.route,
        label = MainRoute.About.title,
        icon = Icons.Outlined.Info,
    ),
    DrawerDestination(
        route = null,
        label = "Logout",
        icon = Icons.AutoMirrored.Outlined.Logout,
    ),
)
