package co.bitfuse.connecthub.data.profile

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import co.bitfuse.connecthub.core.database.ConnectHubDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfileDaoTest {
    private lateinit var database: ConnectHubDatabase
    private lateinit var profileDao: ProfileDao

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(
            context,
            ConnectHubDatabase::class.java,
        ).allowMainThreadQueries().build()
        profileDao = database.profileDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun upsertAndObserveProfile() = runTest {
        profileDao.upsertProfile(testProfile(name = "Alex Morgan"))
        profileDao.upsertProfile(testProfile(name = "Alex Updated"))

        assertEquals("Alex Updated", profileDao.getProfile("user-alex")?.name)
        assertEquals("Alex Updated", profileDao.observeProfile("user-alex").first()?.name)
    }

    private fun testProfile(name: String): ProfileEntity = ProfileEntity(
        id = "user-alex",
        name = name,
        avatarUrl = "https://example.com/avatar.png",
        bio = "Mobile Lead",
        followersCount = 1240,
        followingCount = 318,
    )
}
