package com.example.connecthub.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.connecthub.core.datastore.connectHubDataStore
import com.example.connecthub.data.auth.AuthApiService
import com.example.connecthub.data.auth.FakeAuthApiService
import com.example.connecthub.data.repository.AuthRepositoryImpl
import com.example.connecthub.domain.repository.AuthRepository
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
