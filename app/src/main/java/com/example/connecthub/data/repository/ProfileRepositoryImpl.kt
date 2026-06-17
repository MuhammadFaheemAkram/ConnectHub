package com.example.connecthub.data.repository

import com.example.connecthub.core.common.dispatcher.DispatcherProvider
import com.example.connecthub.data.feed.PostDao
import com.example.connecthub.data.mapper.toDomain
import com.example.connecthub.data.profile.ProfileDao
import com.example.connecthub.data.profile.ProfileEntity
import com.example.connecthub.domain.model.Post
import com.example.connecthub.domain.model.User
import com.example.connecthub.domain.repository.ProfileRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ProfileRepositoryImpl @Inject constructor(
    private val profileDao: ProfileDao,
    private val postDao: PostDao,
    private val dispatchers: DispatcherProvider,
) : ProfileRepository {
    override fun observeProfile(): Flow<User> {
        return profileDao.observeProfile(profileId).filterNotNull().map { it.toDomain() }
    }

    override fun observeUserPosts(): Flow<List<Post>> {
        return postDao.observePostsByAuthor(profileId).map { posts ->
            posts.map { it.toDomain() }
        }
    }

    override suspend fun ensureProfile() {
        withContext(dispatchers.io) {
            if (profileDao.getProfile(profileId) == null) {
                profileDao.upsertProfile(defaultProfile)
            }
        }
    }

    override suspend fun updateProfile(name: String, bio: String, avatarUrl: String) {
        withContext(dispatchers.io) {
            val current = profileDao.getProfile(profileId) ?: defaultProfile
            profileDao.upsertProfile(
                current.copy(
                    name = name.trim(),
                    bio = bio.trim(),
                    avatarUrl = avatarUrl.trim(),
                ),
            )
        }
    }

    private companion object {
        const val profileId = "user-alex"
        val defaultProfile = ProfileEntity(
            id = profileId,
            name = "Alex Morgan",
            avatarUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9",
            bio = "Building thoughtful Android apps with Compose.",
            followersCount = 1240,
            followingCount = 318,
        )
    }
}
