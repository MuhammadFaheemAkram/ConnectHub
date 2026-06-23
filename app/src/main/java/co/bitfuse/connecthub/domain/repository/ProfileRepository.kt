package co.bitfuse.connecthub.domain.repository

import co.bitfuse.connecthub.domain.model.Post
import co.bitfuse.connecthub.domain.model.User
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun observeProfile(): Flow<User>
    fun observeUserPosts(): Flow<List<Post>>
    suspend fun ensureProfile()
    suspend fun updateProfile(name: String, bio: String, avatarUrl: String)
}
