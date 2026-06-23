package co.bitfuse.connecthub.di

import co.bitfuse.connecthub.data.chat.ChatApiService
import co.bitfuse.connecthub.data.chat.FakeChatApiService
import co.bitfuse.connecthub.data.notification.FakeNotificationApiService
import co.bitfuse.connecthub.data.notification.NotificationApiService
import co.bitfuse.connecthub.data.repository.ChatRepositoryImpl
import co.bitfuse.connecthub.data.repository.NotificationRepositoryImpl
import co.bitfuse.connecthub.data.repository.SettingsRepositoryImpl
import co.bitfuse.connecthub.domain.repository.ChatRepository
import co.bitfuse.connecthub.domain.repository.NotificationRepository
import co.bitfuse.connecthub.domain.repository.SettingsRepository
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
