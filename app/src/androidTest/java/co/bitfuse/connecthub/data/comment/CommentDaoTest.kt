package co.bitfuse.connecthub.data.comment

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import co.bitfuse.connecthub.core.database.ConnectHubDatabase
import co.bitfuse.connecthub.data.feed.PostEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CommentDaoTest {
    private lateinit var database: ConnectHubDatabase
    private lateinit var commentDao: CommentDao

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(
            context,
            ConnectHubDatabase::class.java,
        ).allowMainThreadQueries().build()
        runBlocking {
            database.postDao().upsertPost(testPostEntity())
        }
        commentDao = database.commentDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndReadComments() = runTest {
        commentDao.upsertComments(listOf(testCommentEntity(id = "comment-1")))

        val comments = commentDao.observeComments("post-1").first()

        assertEquals(listOf("comment-1"), comments.map { it.id })
    }

    @Test
    fun deleteOwnCommentOnlyDeletesOwnRows() = runTest {
        commentDao.upsertComments(
            listOf(
                testCommentEntity(id = "own-comment", isOwnComment = true),
                testCommentEntity(id = "other-comment", isOwnComment = false),
            ),
        )

        commentDao.deleteOwnComment("own-comment")
        commentDao.deleteOwnComment("other-comment")

        val comments = commentDao.observeComments("post-1").first()
        assertEquals(listOf("other-comment"), comments.map { it.id })
    }

    private fun testPostEntity(): PostEntity = PostEntity(
        id = "post-1",
        authorId = "user-1",
        authorName = "Maya Chen",
        authorAvatarUrl = "",
        authorBio = "Android Engineer",
        authorFollowersCount = 10,
        authorFollowingCount = 4,
        content = "Cached post",
        imageUrl = null,
        createdAt = 1_000L,
        likeCount = 4,
        commentCount = 2,
        isLiked = false,
        isBookmarked = false,
        page = 1,
    )

    private fun testCommentEntity(
        id: String,
        isOwnComment: Boolean = false,
    ): CommentEntity = CommentEntity(
        id = id,
        postId = "post-1",
        authorId = "user-1",
        authorName = "Omar Malik",
        authorAvatarUrl = "",
        authorBio = "Product Designer",
        authorFollowersCount = 10,
        authorFollowingCount = 4,
        text = "Helpful comment",
        createdAt = 1_000L,
        isOwnComment = isOwnComment,
    )
}
