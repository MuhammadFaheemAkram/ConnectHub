package co.bitfuse.connecthub.data.auth

data class UserDto(
    val id: String,
    val name: String,
    val email: String,
    val avatarUrl: String,
    val bio: String,
    val followersCount: Int,
    val followingCount: Int,
)
