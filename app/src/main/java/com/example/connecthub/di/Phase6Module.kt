package com.example.connecthub.di

import com.example.connecthub.data.chat.ChatApiService
import com.example.connecthub.data.chat.FakeChatApiService
import com.example.connecthub.data.notification.FakeNotificationApiService
import com.example.connecthub.data.notification.NotificationApiService
import com.example.connecthub.data.repository.ChatRepositoryImpl
import com.example.connecthub.data.repository.NotificationRepositoryImpl
import com.example.connecthub.data.repository.SettingsRepositoryImpl
import com.example.connecthub.domain.repository.ChatRepository
import com.example.connecthub.domain.repository.NotificationRepository
import com.example.connecthub.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class Phase6Module {
    @Binds
    @Singleton
    abstract fun bindChatApiService(implementation: FakeChatApiService): ChatApiService

    @Binds
    @Singleton
    abstract fun bindNotificationApiService(implementation: FakeNotificationApiService): NotificationApiService

    @Binds
    @Singleton
    abstract fun bindChatRepository(implementation: ChatRepositoryImpl): ChatRepository

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(implementation: NotificationRepositoryImpl): NotificationRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(implementation: SettingsRepositoryImpl): SettingsRepository
}
