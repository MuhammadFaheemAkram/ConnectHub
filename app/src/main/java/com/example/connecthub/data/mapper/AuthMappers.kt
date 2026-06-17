package com.example.connecthub.data.mapper

import com.example.connecthub.data.auth.AuthDto
import com.example.connecthub.data.auth.UserDto
import com.example.connecthub.domain.model.AuthSession
import com.example.connecthub.domain.model.User

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
