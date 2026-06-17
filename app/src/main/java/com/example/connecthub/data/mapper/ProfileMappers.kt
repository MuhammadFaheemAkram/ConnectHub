package com.example.connecthub.data.mapper

import com.example.connecthub.data.profile.ProfileEntity
import com.example.connecthub.domain.model.User

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
