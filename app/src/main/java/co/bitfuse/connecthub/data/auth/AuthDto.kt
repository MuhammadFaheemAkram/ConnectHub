package co.bitfuse.connecthub.data.auth

data class AuthDto(
    val token: String,
    val user: UserDto,
)
