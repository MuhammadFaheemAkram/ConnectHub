package com.example.connecthub.profile

import com.example.connecthub.domain.model.Post
import com.example.connecthub.domain.model.User
import com.example.connecthub.domain.repository.ProfileRepository
import com.example.connecthub.feed.FeedTestData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeProfileRepository(
    initialUser: User = sampleUser(),
    initialPosts: List<Post> = emptyList(),
) : ProfileRepository {
    private val profile = MutableStateFlow(initialUser)
    private val posts = MutableStateFlow(initialPosts)
    var ensuredProfile = false
    var updatedName: String? = null
    var updatedBio: String? = null
    var updatedAvatarUrl: String? = null

    override fun observeProfile(): Flow<User> = profile

    override fun observeUserPosts(): Flow<List<Post>> = posts

    override suspend fun ensureProfile() {
        ensuredProfile = true
    }

    override suspend fun updateProfile(name: String, bio: String, avatarUrl: String) {
        updatedName = name
        updatedBio = bio
        updatedAvatarUrl = avatarUrl
        profile.value = profile.value.copy(
            name = name,
            bio = bio,
            avatarUrl = avatarUrl,
        )
    }

    companion object {
        fun sampleUser(): User = FeedTestData.user(id = "user-alex").copy(
            name = "Alex Morgan",
            avatarUrl = "https://example.com/alex.png",
            bio = "Mobile Lead",
            followersCount = 1240,
            followingCount = 318,
        )
    }
}
