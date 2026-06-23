package co.bitfuse.connecthub.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import co.bitfuse.connecthub.domain.model.AuthSession
import co.bitfuse.connecthub.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionLocalDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    val session: Flow<AuthSession?> = dataStore.data.map { preferences ->
        val token = preferences[Keys.token].orEmpty()
        val userId = preferences[Keys.userId].orEmpty()
        if (token.isBlank() || userId.isBlank()) {
            null
        } else {
            AuthSession(
                token = token,
                userId = userId,
                user = User(
                    id = userId,
                    name = preferences[Keys.name].orEmpty(),
                    avatarUrl = preferences[Keys.avatarUrl].orEmpty(),
                    bio = preferences[Keys.bio].orEmpty(),
                    followersCount = preferences[Keys.followersCount] ?: 0,
                    followingCount = preferences[Keys.followingCount] ?: 0,
                ),
            )
        }
    }

    suspend fun saveSession(session: AuthSession) {
        dataStore.edit { preferences ->
            preferences[Keys.token] = session.token
            preferences[Keys.userId] = session.userId
            preferences[Keys.name] = session.user.name
            preferences[Keys.avatarUrl] = session.user.avatarUrl
            preferences[Keys.bio] = session.user.bio
            preferences[Keys.followersCount] = session.user.followersCount
            preferences[Keys.followingCount] = session.user.followingCount
            preferences[Keys.firstLaunch] = false
        }
    }

    suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences.remove(Keys.token)
            preferences.remove(Keys.userId)
            preferences.remove(Keys.name)
            preferences.remove(Keys.avatarUrl)
            preferences.remove(Keys.bio)
            preferences.remove(Keys.followersCount)
            preferences.remove(Keys.followingCount)
        }
    }

    private object Keys {
        val token = stringPreferencesKey("auth_token")
        val userId = stringPreferencesKey("current_user_id")
        val name = stringPreferencesKey("current_user_name")
        val avatarUrl = stringPreferencesKey("current_user_avatar_url")
        val bio = stringPreferencesKey("current_user_bio")
        val followersCount = intPreferencesKey("current_user_followers_count")
        val followingCount = intPreferencesKey("current_user_following_count")
        val firstLaunch = androidx.datastore.preferences.core.booleanPreferencesKey("first_launch")
    }
}
