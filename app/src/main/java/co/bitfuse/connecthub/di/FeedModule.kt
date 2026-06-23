package co.bitfuse.connecthub.di

import co.bitfuse.connecthub.data.comment.CommentApiService
import co.bitfuse.connecthub.data.comment.FakeCommentApiService
import co.bitfuse.connecthub.data.feed.FakeFeedApiService
import co.bitfuse.connecthub.data.feed.FeedApiService
import co.bitfuse.connecthub.data.repository.CommentRepositoryImpl
import co.bitfuse.connecthub.data.repository.FeedRepositoryImpl
import co.bitfuse.connecthub.data.repository.PostRepositoryImpl
import co.bitfuse.connecthub.domain.repository.CommentRepository
import co.bitfuse.connecthub.domain.repository.FeedRepository
import co.bitfuse.connecthub.domain.repository.PostRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FeedModule {
    @Binds
    @Singleton
    abstract fun bindFeedApiService(implementation: FakeFeedApiService): FeedApiService

    @Binds
    @Singleton
    abstract fun bindCommentApiService(implementation: FakeCommentApiService): CommentApiService

    @Binds
    @Singleton
    abstract fun bindFeedRepository(implementation: FeedRepositoryImpl): FeedRepository

    @Binds
    @Singleton
    abstract fun bindPostRepository(implementation: PostRepositoryImpl): PostRepository

    @Binds
    @Singleton
    abstract fun bindCommentRepository(implementation: CommentRepositoryImpl): CommentRepository
}
