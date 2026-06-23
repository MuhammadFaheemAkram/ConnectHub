package co.bitfuse.connecthub.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import co.bitfuse.connecthub.core.datastore.connectHubDataStore
import co.bitfuse.connecthub.data.auth.AuthApiService
import co.bitfuse.connecthub.data.auth.FakeAuthApiService
import co.bitfuse.connecthub.data.repository.AuthRepositoryImpl
import co.bitfuse.connecthub.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(implementation: AuthRepositoryImpl): AuthRepository
}

@Module
@InstallIn(SingletonComponent::class)
object AuthDataModule {
    @Provides
    @Singleton
    fun providePreferencesDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = context.connectHubDataStore

    @Provides
    @Singleton
    fun provideAuthApiService(): AuthApiService = FakeAuthApiService()
}
