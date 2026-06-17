package com.example.connecthub.data.auth

import com.example.connecthub.core.network.FakeNetworkConfig
import kotlinx.coroutines.delay
import javax.inject.Inject

class FakeAuthApiService @Inject constructor() : AuthApiService {
    override suspend fun login(email: String, password: String): AuthDto {
        delay(FakeNetworkConfig.defaultDelayMillis)
        maybeThrowSimulatedError()

        return AuthDto(
            token = "fake-token-${email.hashCode()}",
            user = sampleUser(
                id = "user-${email.lowercase().hashCode()}",
                name = email.substringBefore("@").replaceFirstChar { it.uppercaseChar() },
                email = email,
            ),
        )
    }

    override suspend fun signUp(name: String, email: String, password: String): AuthDto {
        delay(FakeNetworkConfig.defaultDelayMillis)
        maybeThrowSimulatedError()

        return AuthDto(
            token = "fake-token-${email.hashCode()}",
            user = sampleUser(
                id = "user-${email.lowercase().hashCode()}",
                name = name,
                email = email,
            ),
        )
    }

    private fun maybeThrowSimulatedError() {
        if (FakeNetworkConfig.errorSimulationEnabled) {
            error("Fake auth service is simulating a network failure.")
        }
    }

    private fun sampleUser(
        id: String,
        name: String,
        email: String,
    ): UserDto = UserDto(
        id = id,
        name = name.ifBlank { "ConnectHub User" },
        email = email,
        avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330",
        bio = "Building thoughtful Android apps with Compose.",
        followersCount = 1240,
        followingCount = 318,
    )
}
