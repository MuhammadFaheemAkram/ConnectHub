package co.bitfuse.connecthub.domain.model

data class User(
    val id: String,
    val name: String,
    val avatarUrl: String,
    val bio: String,
    val followersCount: Int,
    val followingCount: Int,
)
