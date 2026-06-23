package co.bitfuse.connecthub.core.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import co.bitfuse.connecthub.core.designsystem.component.ConnectHubBottomBar
import co.bitfuse.connecthub.core.designsystem.component.ConnectHubTopBar
import co.bitfuse.connecthub.core.designsystem.component.ConnectHubTopBarNavigationType
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectHubMainScaffold(
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: MainRoute.Feed.route
    val currentDestination = MainRoute.all.firstOrNull { it.route == currentRoute } ?: MainRoute.Feed
    val bottomRoutes = MainRoute.bottomBarRoutes.map { it.route }.toSet()
    val drawerRoutes = MainRoute.drawerRoutes.map { it.route }.toSet()
    val canOpenDrawer = currentRoute in drawerRoutes

    fun navigateTo(route: String) {
        scope.launch { drawerState.close() }
        if (currentRoute != route) {
            navController.navigate(route) {
                launchSingleTop = true
                restoreState = true
                popUpTo(MainRoute.Feed.route) {
                    saveState = true
                }
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(vertical = 12.dp),
                ) {
                    Text(
                        text = "ConnectHub",
                        modifier = Modifier.padding(horizontal = 28.dp, vertical = 16.dp),
                    )
                    HorizontalDivider()
                    ConnectHubDrawerDestinations.forEach { destination ->
                        val isLogout = destination.route == null
                        NavigationDrawerItem(
                            label = { Text(destination.label) },
                            selected = destination.route == currentRoute,
                            icon = {
                                Icon(
                                    imageVector = destination.icon,
                                    contentDescription = destination.label,
                                )
                            },
                            onClick = {
                                if (isLogout) {
                                    scope.launch { drawerState.close() }
                                    onLogout()
                                } else {
                                    destination.route?.let(::navigateTo)
                                }
                            },
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp),
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        },
        modifier = modifier,
    ) {
        Scaffold(
            topBar = {
                ConnectHubTopBar(
                    title = currentDestination.title,
                    navigationType = if (canOpenDrawer) {
                        ConnectHubTopBarNavigationType.Drawer
                    } else {
                        ConnectHubTopBarNavigationType.Back
                    },
                    onNavigationClick = {
                        if (canOpenDrawer) {
                            scope.launch { drawerState.open() }
                        } else {
                            navController.popBackStack()
                        }
                    },
                )
            },
            bottomBar = {
                if (currentRoute in bottomRoutes) {
                    ConnectHubBottomBar(
                        items = ConnectHubBottomDestinations,
                        currentRoute = currentRoute,
                        onItemClick = { navigateTo(it.route) },
                    )
                }
            },
            floatingActionButton = {
                if (currentRoute == MainRoute.Feed.route) {
                    FloatingActionButton(
                        onClick = { navController.navigate(MainRoute.CreatePost.route) },
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = "Create post",
                        )
                    }
                }
            },
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = MainRoute.Feed.route,
                modifier = Modifier.padding(innerPadding),
            ) {
                mainGraph(
                    navController = navController,
                    onLogout = onLogout,
                )
            }
        }
    }
}
