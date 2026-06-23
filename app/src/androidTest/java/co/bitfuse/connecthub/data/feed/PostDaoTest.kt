package co.bitfuse.connecthub.data.feed

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
class PostDaoTest {
    private lateinit var database: ConnectHubDatabase
    private lateinit var postDao: PostDao

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(
            context,
            ConnectHubDatabase::class.java,
        ).allowMainThreadQueries().build()
        postDao = database.postDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndReadPosts() = runTest {
        postDao.upsertPosts(listOf(testPostEntity(id = "post-1")))

        val posts = postDao.observeFeed().first()

        assertEquals(listOf("post-1"), posts.map { it.id })
    }

    @Test
    fun bookmarkAndUnbookmarkPost() = runTest {
        postDao.upsertPost(testPostEntity(id = "post-1"))

        postDao.setBookmarkState(postId = "post-1", isBookmarked = true, bookmarkedAt = 1_000L)

        assertEquals(listOf("post-1"), postDao.getBookmarkedPostIds())
        assertEquals(listOf("post-1"), postDao.observeBookmarkedPosts().first().map { it.id })

        postDao.setBookmarkState(postId = "post-1", isBookmarked = false)

        assertEquals(emptyList<String>(), postDao.getBookmarkedPostIds())
        assertEquals(emptyList<PostEntity>(), postDao.observeBookmarkedPosts().first())
    }

    @Test
    fun searchPostsMatchesContentAndAuthor() = runTest {
        postDao.upsertPosts(
            listOf(
                testPostEntity(id = "post-compose", content = "Compose state patterns"),
                testPostEntity(id = "post-design", authorName = "Omar Malik", content = "Design notes"),
            ),
        )

        assertEquals(listOf("post-compose"), postDao.searchPosts("compose").first().map { it.id })
        assertEquals(listOf("post-design"), postDao.searchPosts("omar").first().map { it.id })
    }

    @Test
    fun observePostsByAuthorFiltersCachedPosts() = runTest {
        postDao.upsertPosts(
            listOf(
                testPostEntity(id = "post-alex", authorId = "user-alex"),
                testPostEntity(id = "post-maya", authorId = "user-maya"),
            ),
        )

        assertEquals(listOf("post-alex"), postDao.observePostsByAuthor("user-alex").first().map { it.id })
    }

    private fun testPostEntity(
        id: String,
        authorId: String = "user-1",
        authorName: String = "Maya Chen",
        content: String = "Cached post",
    ): PostEntity = PostEntity(
        id = id,
        authorId = authorId,
        authorName = authorName,
        authorAvatarUrl = "",
        authorBio = "Android Engineer",
        authorFollowersCount = 10,
        authorFollowingCount = 4,
        content = content,
        imageUrl = null,
        createdAt = 1_000L,
        likeCount = 4,
        commentCount = 2,
        isLiked = false,
        isBookmarked = false,
        page = 1,
    )
}
