package com.example.connecthub.data.search

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.connecthub.core.database.ConnectHubDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecentSearchDaoTest {
    private lateinit var database: ConnectHubDatabase
    private lateinit var recentSearchDao: RecentSearchDao

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(
            context,
            ConnectHubDatabase::class.java,
        ).allowMainThreadQueries().build()
        recentSearchDao = database.recentSearchDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertReadAndClearRecentSearches() = runTest {
        recentSearchDao.upsertRecentSearch(RecentSearchEntity(query = "compose", searchedAt = 1_000L))
        recentSearchDao.upsertRecentSearch(RecentSearchEntity(query = "android", searchedAt = 2_000L))

        assertEquals(listOf("android", "compose"), recentSearchDao.observeRecentSearches().first().map { it.query })

        recentSearchDao.clearRecentSearches()

        assertEquals(emptyList<String>(), recentSearchDao.observeRecentSearches().first().map { it.query })
    }
}
