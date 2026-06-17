package com.example.connecthub.core.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.connecthub.feature.bookmarks.BookmarksRoute
import com.example.connecthub.feature.chatdetail.ChatDetailRoute
import com.example.connecthub.feature.chatlist.ChatListRoute
import com.example.connecthub.feature.comments.CommentsScreen
import com.example.connecthub.feature.createpost.CreatePostScreen
import com.example.connecthub.feature.feed.FeedRoute
import com.example.connecthub.feature.notifications.NotificationsRoute
import com.example.connecthub.feature.postdetail.PostDetailScreen
import com.example.connecthub.feature.profile.EditProfileRoute
import com.example.connecthub.feature.profile.ProfileRoute
import com.example.connecthub.feature.search.SearchRoute
import com.example.connecthub.feature.settings.AboutScreen
import com.example.connecthub.feature.settings.SettingsRoute

fun NavGraphBuilder.mainGraph(
    navController: NavHostController,
    onLogout: () -> Unit,
) {
    composable(MainRoute.Feed.route) {
        FeedRoute(
            onOpenCreatePost = { navController.navigate(MainRoute.CreatePost.route) },
            onOpenPost = { postId -> navController.navigate(MainRoute.PostDetail.createRoute(postId)) },
            onOpenComments = { postId -> navController.navigate(MainRoute.Comments.createRoute(postId)) },
        )
    }
    composable(MainRoute.Search.route) {
        SearchRoute(
            onOpenPost = { postId -> navController.navigate(MainRoute.PostDetail.createRoute(postId)) },
        )
    }
    composable(MainRoute.Bookmarks.route) {
        BookmarksRoute(
            onOpenPost = { postId -> navController.navigate(MainRoute.PostDetail.createRoute(postId)) },
        )
    }
    composable(MainRoute.Notifications.route) {
        NotificationsRoute(
            onOpenPost = { postId -> navController.navigate(MainRoute.PostDetail.createRoute(postId)) },
        )
    }
    composable(MainRoute.Profile.route) {
        ProfileRoute(
            onEditProfile = { navController.navigate(MainRoute.EditProfile.route) },
            onOpenPost = { postId -> navController.navigate(MainRoute.PostDetail.createRoute(postId)) },
            onLogout = onLogout,
        )
    }
    composable(MainRoute.ChatList.route) {
        ChatListRoute(
            onOpenConversation = { conversationId -> navController.navigate(MainRoute.ChatDetail.createRoute(conversationId)) },
        )
    }
    composable(
        route = MainRoute.ChatDetail.route,
        arguments = listOf(navArgument(MainRoute.ChatDetail.conversationIdArg) { type = NavType.StringType }),
    ) { backStackEntry ->
        ChatDetailRoute(
            conversationId = backStackEntry.arguments?.getString(MainRoute.ChatDetail.conversationIdArg).orEmpty(),
        )
    }
    composable(MainRoute.Settings.route) {
        SettingsRoute(onLogout = onLogout)
    }
    composable(MainRoute.About.route) {
        AboutScreen()
    }
    composable(MainRoute.CreatePost.route) {
        CreatePostScreen(
            onPostCreated = {
                navController.popBackStack()
            },
        )
    }
    composable(
        route = MainRoute.PostDetail.route,
        arguments = listOf(navArgument(MainRoute.PostDetail.postIdArg) { type = NavType.StringType }),
    ) { backStackEntry ->
        val postId = backStackEntry.arguments?.getString(MainRoute.PostDetail.postIdArg).orEmpty()
        PostDetailScreen(
            postId = postId,
            onOpenComments = { navController.navigate(MainRoute.Comments.createRoute(postId)) },
        )
    }
    composable(
        route = MainRoute.Comments.route,
        arguments = listOf(navArgument(MainRoute.Comments.postIdArg) { type = NavType.StringType }),
    ) { backStackEntry ->
        CommentsScreen(
            postId = backStackEntry.arguments?.getString(MainRoute.Comments.postIdArg).orEmpty(),
        )
    }
    composable(MainRoute.EditProfile.route) {
        EditProfileRoute(
            onSaved = { navController.popBackStack() },
        )
    }
}
