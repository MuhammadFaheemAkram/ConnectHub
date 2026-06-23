package co.bitfuse.connecthub.core.designsystem.component

import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

data class ConnectHubNavigationItem(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector = selectedIcon,
    val badgeCount: Int? = null,
)

@Composable
fun ConnectHubBottomBar(
    items: List<ConnectHubNavigationItem>,
    currentRoute: String?,
    onItemClick: (ConnectHubNavigationItem) -> Unit,
) {
    NavigationBar {
        items.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick = { onItemClick(item) },
                icon = {
                    BadgedBox(
                        badge = {
                            val badgeCount = item.badgeCount
                            if (badgeCount != null && badgeCount > 0) {
                                Badge { Text(text = badgeCount.toString()) }
                            }
                        },
                    ) {
                        Icon(
                            imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.label,
                        )
                    }
                },
                label = { Text(text = item.label) },
            )
        }
    }
}
