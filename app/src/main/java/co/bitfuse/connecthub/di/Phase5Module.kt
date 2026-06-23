package co.bitfuse.connecthub.di

import co.bitfuse.connecthub.data.repository.BookmarkRepositoryImpl
import co.bitfuse.connecthub.data.repository.ProfileRepositoryImpl
import co.bitfuse.connecthub.data.repository.SearchRepositoryImpl
import co.bitfuse.connecthub.domain.repository.BookmarkRepository
import co.bitfuse.connecthub.domain.repository.ProfileRepository
import co.bitfuse.connecthub.domain.repository.SearchRepository
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
