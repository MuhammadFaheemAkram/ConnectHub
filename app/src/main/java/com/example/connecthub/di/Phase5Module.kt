package com.example.connecthub.di

import com.example.connecthub.data.repository.BookmarkRepositoryImpl
import com.example.connecthub.data.repository.ProfileRepositoryImpl
import com.example.connecthub.data.repository.SearchRepositoryImpl
import com.example.connecthub.domain.repository.BookmarkRepository
import com.example.connecthub.domain.repository.ProfileRepository
import com.example.connecthub.domain.repository.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class Phase5Module {
    @Binds
    @Singleton
    abstract fun bindSearchRepository(implementation: SearchRepositoryImpl): SearchRepository

    @Binds
    @Singleton
    abstract fun bindBookmarkRepository(implementation: BookmarkRepositoryImpl): BookmarkRepository

    @Binds
    @Singleton
    abstract fun bindProfileRepository(implementation: ProfileRepositoryImpl): ProfileRepository
}
