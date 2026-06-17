package com.example.connecthub.core.navigation

sealed interface ConnectHubRoute {
    val route: String
    val title: String
}

sealed class RootRoute(
    override val route: String,
    override val title: String,
) : ConnectHubRoute {
    data object Splash : RootRoute("splash", "ConnectHub")
    data object AuthGraph : RootRoute("auth_graph", "Auth")
    data object MainGraph : RootRoute("main_graph", "ConnectHub")
}

sealed class AuthRoute(
    override val route: String,
    override val title: String,
) : ConnectHubRoute {
    data object Login : AuthRoute("login", "Sign in")
    data object SignUp : AuthRoute("sign_up", "Create account")
}

sealed class MainRoute(
    override val route: String,
    override val title: String,
) : ConnectHubRoute {
    data object Feed : MainRoute("feed", "Feed")
    data object Search : MainRoute("search", "Search")
    data object Bookmarks : MainRoute("bookmarks", "Bookmarks")
    data object Notifications : MainRoute("notifications", "Notifications")
    data object Profile : MainRoute("profile", "Profile")
    data object ChatList : MainRoute("chats", "Chats")
    data object ChatDetail : MainRoute("chat_detail/{conversationId}", "Conversation") {
        const val conversationIdArg = "conversationId"
        fun createRoute(conversationId: String): String = "chat_detail/$conversationId"
    }
    data object Settings : MainRoute("settings", "Settings")
    data object About : MainRoute("about", "About")
    data object CreatePost : MainRoute("create_post", "Create post")
    data object PostDetail : MainRoute("post_detail/{postId}", "Post") {
        const val postIdArg = "postId"
        fun createRoute(postId: String): String = "post_detail/$postId"
    }

    data object Comments : MainRoute("comments/{postId}", "Comments") {
        const val postIdArg = "postId"
        fun createRoute(postId: String): String = "comments/$postId"
    }
    data object EditProfile : MainRoute("edit_profile", "Edit profile")

    companion object {
        val bottomBarRoutes: List<MainRoute>
            get() = listOf(Feed, Search, Bookmarks, Notifications, Profile)

        val drawerRoutes: List<MainRoute>
            get() = listOf(Feed, ChatList, Settings, About)

        val all: List<MainRoute>
            get() = listOf(
                Feed,
                Search,
                Bookmarks,
                Notifications,
                Profile,
                ChatList,
                ChatDetail,
                Settings,
                About,
                CreatePost,
                PostDetail,
                Comments,
                EditProfile,
            )
    }
}
