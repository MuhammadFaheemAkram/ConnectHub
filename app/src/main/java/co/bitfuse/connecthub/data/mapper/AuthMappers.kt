package co.bitfuse.connecthub.data.mapper

import co.bitfuse.connecthub.data.auth.AuthDto
import co.bitfuse.connecthub.data.auth.UserDto
import co.bitfuse.connecthub.domain.model.AuthSession
import co.bitfuse.connecthub.domain.model.User

fun AuthDto.toDomain(): AuthSession = AuthSession(
    token = token,
    userId = user.id,
    user = user.toDomain(),
)

fun UserDto.toDomain(): User = User(
    id = id,
    name = name,
    avatarUrl = avatarUrl,
    bio = bio,
    followersCount = followersCount,
    followingCount = followingCount,
)
