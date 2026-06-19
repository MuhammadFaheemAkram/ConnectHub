# Phase 6 Summary: Chat, Notifications, And Settings

## Phase Goal

Complete the main product surface with cached conversations/messages, in-app notifications, and persisted app preferences that immediately affect the theme.

## What Was Implemented

- Chat DTOs, entities, DAO, fake API, repository, use cases, ViewModels, and screens
- Conversation search and unread badges
- Message bubbles, send input, typing indicator, and delayed fake reply
- Notification DTO/entity/DAO/fake API/repository/use cases/ViewModel/screen
- Notification category filters, unread filter, mark-read, and mark-all-read
- Preferences DataStore settings source
- Dark mode, dynamic color, notifications, language, cache action, and logout UI
- Activity-level settings ViewModel to update theme immediately
- Room migration from version 3 to 4
- Unit tests, DataStore repository test, Room tests, and Compose tests

## Android Concepts Demonstrated

- Parent/child Room tables and foreign keys
- Multiple live Flow screens
- Simulated long-running send/reply work
- Derived filter/search state
- Activity-level StateFlow observation
- DataStore preference updates
- Dynamic Material color support
- Hilt bindings for multiple feature repositories/services

## Data Flow

Send message:

```text
ChatDetailScreen send
  -> ChatDetailViewModel.send
  -> SendMessageUseCase
  -> ChatRepositoryImpl
  -> FakeChatApiService.sendMessage
  -> ChatDao upsert sent message
  -> delay and build fake reply
  -> ChatDao upsert reply
  -> messages Flow emits both updates
  -> ChatDetailUiState updates
```

Theme update:

```text
Settings switch
  -> SettingsViewModel.updateTheme
  -> UpdateThemeUseCase
  -> SettingsRepositoryImpl
  -> SettingsLocalDataSource DataStore edit
  -> AppSettingsViewModel observes settings Flow
  -> MainActivity passes darkTheme to ConnectHubTheme
```

## Important Files

- `data/chat/ChatApiService.kt`: chat remote contract.
- `data/chat/FakeChatApiService.kt`: seeded conversations/messages.
- `data/chat/ChatDao.kt`: conversation/message observation and writes.
- `data/repository/ChatRepositoryImpl.kt`: cache refresh and fake reply.
- `feature/chatlist/ChatListViewModel.kt`: conversation list/search state.
- `feature/chatdetail/ChatDetailViewModel.kt`: load, draft, send, and typing state.
- `data/notification/NotificationDao.kt`: notification observation/read mutations.
- `data/repository/NotificationRepositoryImpl.kt`: refresh preserving read ids.
- `feature/notifications/NotificationsViewModel.kt`: filtering and read actions.
- `core/datastore/SettingsLocalDataSource.kt`: settings preferences.
- `feature/settings/SettingsViewModel.kt`: settings intents/effects.
- `feature/settings/AppSettingsViewModel.kt`: app-level theme state.
- `MainActivity.kt`: applies persisted theme settings.

## Important Classes

### `ChatRepositoryImpl`

Coordinates fake API and Room, persists outgoing messages, creates a delayed incoming reply, and updates conversation previews.

### `NotificationsUiState`

Derives unread count and category-filtered notifications from one durable list and selected filter.

### `AppSettingsViewModel`

Converts the DataStore-backed settings Flow into StateFlow consumed by `MainActivity`.

## Interview Notes

### Why is the typing indicator in ViewModel state?

It represents current screen behavior during the suspended send/reply operation and must survive recomposition.

### How is notification read state preserved during refresh?

The repository reads locally read ids before mapping refreshed DTOs and uses those ids as an override.

### Does this phase use WorkManager or Android notifications?

No. It implements an in-app notification feed. WorkManager, FCM, NotificationManager, and runtime notification permission are documented future improvements.

## Learning Checklist

- [ ] I can trace outgoing and fake incoming messages.
- [ ] I understand the message foreign key.
- [ ] I can explain derived notification filters.
- [ ] I can trace dark mode from switch to activity theme.
- [ ] I understand DataStore versus Room responsibilities.
- [ ] I can find the version 3-to-4 migration.

## Future Improvements

- Add WorkManager periodic notification refresh.
- Add Android system notifications and runtime permission handling.
- Mark conversation unread counts dynamically when replies arrive off-screen.
- Add delivery/sending/failed message states.
- Implement a real cache-clear manager.
- Add WebSocket/real-time backend integration.
