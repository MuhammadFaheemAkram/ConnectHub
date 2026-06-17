package com.example.connecthub.data.repository

import com.example.connecthub.core.common.dispatcher.DispatcherProvider
import com.example.connecthub.data.feed.PostDao
import com.example.connecthub.data.mapper.toDomain
import com.example.connecthub.data.search.RecentSearchDao
import com.example.connecthub.data.search.RecentSearchEntity
import com.example.connecthub.domain.model.SearchResults
import com.example.connecthub.domain.model.User
import com.example.connecthub.domain.repository.SearchRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class SearchRepositoryImpl @Inject constructor(
    private val postDao: PostDao,
    private val recentSearchDao: RecentSearchDao,
    private val dispatchers: DispatcherProvider,
) : SearchRepository {
    override fun search(query: String): Flow<SearchResults> {
        val trimmedQuery = query.trim()
        return postDao.searchPosts(trimmedQuery).map { posts ->
            if (trimmedQuery.isBlank()) {
                SearchResults()
            } else {
                SearchResults(
                    users = sampleUsers.filter { user ->
                        user.name.contains(trimmedQuery, ignoreCase = true) ||
                            user.bio.contains(trimmedQuery, ignoreCase = true)
                    },
                    posts = posts.map { it.toDomain() },
                )
            }
        }
    }

    override fun observeRecentSearches(): Flow<List<String>> {
        return recentSearchDao.observeRecentSearches().map { searches ->
            searches.map { it.query }
        }
    }

    override suspend fun saveRecentSearch(query: String) {
        val trimmedQuery = query.trim()
        if (trimmedQuery.isBlank()) return
        withContext(dispatchers.io) {
            recentSearchDao.upsertRecentSearch(
                RecentSearchEntity(
                    query = trimmedQuery,
                    searchedAt = System.currentTimeMillis(),
                ),
            )
        }
    }

    override suspend fun clearRecentSearches() {
        withContext(dispatchers.io) {
            recentSearchDao.clearRecentSearches()
        }
    }

    private companion object {
        val sampleUsers = listOf(
            User(
                id = "user-maya",
                name = "Maya Chen",
                avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330",
                bio = "Android Engineer",
                followersCount = 8200,
                followingCount = 312,
            ),
            User(
                id = "user-omar",
                name = "Omar Malik",
                avatarUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e",
                bio = "Product Designer",
                followersCount = 5100,
                followingCount = 440,
            ),
            User(
                id = "user-nadia",
                name = "Nadia Khan",
                avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb",
                bio = "Engineering Manager",
                followersCount = 12700,
                followingCount = 540,
            ),
            User(
                id = "user-alex",
                name = "Alex Morgan",
                avatarUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9",
                bio = "Mobile Lead",
                followersCount = 1240,
                followingCount = 318,
            ),
        )
    }
}
