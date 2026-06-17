package com.example.connecthub.di

import com.example.connecthub.data.comment.CommentApiService
import com.example.connecthub.data.comment.FakeCommentApiService
import com.example.connecthub.data.feed.FakeFeedApiService
import com.example.connecthub.data.feed.FeedApiService
import com.example.connecthub.data.repository.CommentRepositoryImpl
import com.example.connecthub.data.repository.FeedRepositoryImpl
import com.example.connecthub.data.repository.PostRepositoryImpl
import com.example.connecthub.domain.repository.CommentRepository
import com.example.connecthub.domain.repository.FeedRepository
import com.example.connecthub.domain.repository.PostRepository
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
