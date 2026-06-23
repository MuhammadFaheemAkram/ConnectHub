package co.bitfuse.connecthub.data.mapper

import co.bitfuse.connecthub.data.profile.ProfileEntity
import co.bitfuse.connecthub.domain.model.User

fun ProfileEntity.toDomain(): User = User(
    id = id,
    name = name,
    avatarUrl = avatarUrl,
    bio = bio,
    followersCount = followersCount,
    followingCount = followingCount,
)

fun User.toProfileEntity(): ProfileEntity = ProfileEntity(
    id = id,
    name = name,
    avatarUrl = avatarUrl,
    bio = bio,
    followersCount = followersCount,
    followingCount = followingCount,
)
