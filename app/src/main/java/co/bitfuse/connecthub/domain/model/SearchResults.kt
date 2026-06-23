package co.bitfuse.connecthub.domain.model

data class SearchResults(
    val users: List<User> = emptyList(),
    val posts: List<Post> = emptyList(),
)
