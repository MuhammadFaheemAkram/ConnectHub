package com.example.connecthub.feed

import com.example.connecthub.core.testing.TestDispatcherProvider
import com.example.connecthub.data.repository.FeedRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FeedRepositoryImplTest {
    @Test
    fun `refreshFeed caches remote posts and exposes them as domain models`() = runTest {
        val repository = FeedRepositoryImpl(
            feedApiService = FakeFeedApi(
                mutableMapOf(1 to listOf(FeedTestData.postDto(id = "post-1"))),
            ),
            postDao = FakePostDao(),
            dispatchers = TestDispatcherProvider(UnconfinedTestDispatcher(testScheduler)),
        )

        val hasMore = repository.refreshFeed(page = 1)
        val posts = repository.observeFeed().first()

        assertTrue(hasMore)
        assertEquals(listOf("post-1"), posts.map { it.id })
    }

    @Test
    fun `likePost updates cached like state`() = runTest {
        val dao = FakePostDao()
        dao.upsertPost(FeedTestData.postEntity(id = "post-1", isLiked = false, likeCount = 4))
        val repository = FeedRepositoryImpl(
            feedApiService = FakeFeedApi(
                mutableMapOf(1 to listOf(FeedTestData.postDto(id = "post-1", isLiked = false, likeCount = 4))),
            ),
            postDao = dao,
            dispatchers = TestDispatcherProvider(UnconfinedTestDispatcher(testScheduler)),
        )

        repository.likePost("post-1")

        val post = repository.observeFeed().first().first()
        assertTrue(post.isLiked)
        assertEquals(5, post.likeCount)
    }

    @Test
    fun `bookmarkPost updates cached bookmark state`() = runTest {
        val dao = FakePostDao()
        dao.upsertPost(FeedTestData.postEntity(id = "post-1", isBookmarked = false))
        val repository = FeedRepositoryImpl(
            feedApiService = FakeFeedApi(
                mutableMapOf(1 to listOf(FeedTestData.postDto(id = "post-1", isBookmarked = false))),
            ),
            postDao = dao,
            dispatchers = TestDispatcherProvider(UnconfinedTestDispatcher(testScheduler)),
        )

        repository.bookmarkPost("post-1")

        val post = repository.observeFeed().first().first()
        assertTrue(post.isBookmarked)
        assertEquals(listOf("post-1"), dao.getBookmarkedPostIds())
    }
}
