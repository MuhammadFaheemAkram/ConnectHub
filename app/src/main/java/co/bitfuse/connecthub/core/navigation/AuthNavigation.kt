package co.bitfuse.connecthub.core.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import co.bitfuse.connecthub.feature.auth.LoginRoute
import co.bitfuse.connecthub.feature.auth.SignUpRoute

fun NavGraphBuilder.authGraph(
    rootNavController: NavController,
) {
    navigation(
        startDestination = AuthRoute.Login.route,
        route = RootRoute.AuthGraph.route,
    ) {
        composable(AuthRoute.Login.route) {
            LoginRoute(
                onLoginSuccess = {
                    rootNavController.navigate(RootRoute.MainGraph.route) {
                        popUpTo(RootRoute.AuthGraph.route) {
                            inclusive = true
                        }
                    }
                },
                onSignUpClick = {
                    rootNavController.navigate(AuthRoute.SignUp.route)
                },
            )
        }
        composable(AuthRoute.SignUp.route) {
            SignUpRoute(
                onSignUpSuccess = {
                    rootNavController.navigate(RootRoute.MainGraph.route) {
                        popUpTo(RootRoute.AuthGraph.route) {
                            inclusive = true
                        }
                    }
                },
                onBackToLoginClick = {
                    rootNavController.popBackStack()
                },
            )
        }
    }
}
