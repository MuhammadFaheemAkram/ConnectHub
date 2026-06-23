package co.bitfuse.connecthub.domain.model

data class AuthSession(
    val token: String,
    val userId: String,
    val user: User,
)
