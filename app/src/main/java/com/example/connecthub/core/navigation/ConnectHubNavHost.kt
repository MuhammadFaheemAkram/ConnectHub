package com.example.connecthub.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.connecthub.feature.auth.SessionViewModel
import com.example.connecthub.feature.auth.SplashRoute

@Composable
fun ConnectHubNavHost(
    modifier: Modifier = Modifier,
) {
    val rootNavController = rememberNavController()
    val sessionViewModel: SessionViewModel = hiltViewModel()

    NavHost(
        navController = rootNavController,
        startDestination = RootRoute.Splash.route,
        modifier = modifier,
    ) {
        composable(RootRoute.Splash.route) {
            SplashRoute(
                onAuthenticated = {
                    rootNavController.navigate(RootRoute.MainGraph.route) {
                        popUpTo(RootRoute.Splash.route) {
                            inclusive = true
                        }
                    }
                },
                onUnauthenticated = {
                    rootNavController.navigate(RootRoute.AuthGraph.route) {
                        popUpTo(RootRoute.Splash.route) {
                            inclusive = true
                        }
                    }
                },
            )
        }
        authGraph(rootNavController)
        composable(RootRoute.MainGraph.route) {
            ConnectHubMainScaffold(
                onLogout = {
                    sessionViewModel.logout()
                    rootNavController.navigate(RootRoute.AuthGraph.route) {
                        popUpTo(RootRoute.MainGraph.route) {
                            inclusive = true
                        }
                    }
                },
            )
        }
    }
}
