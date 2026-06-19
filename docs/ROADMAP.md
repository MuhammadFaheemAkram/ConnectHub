# Roadmap

ConnectHub has completed its seven planned implementation phases. Future work should deepen quality rather than expand the app indiscriminately.

## Completed

- Phase 1: project foundation, design system, navigation shell, and placeholders
- Phase 2: fake auth and DataStore session
- Phase 3: feed, fake API, Room cache, likes, and bookmarks
- Phase 4: post detail, comments, and create post
- Phase 5: search, recent searches, bookmarks, profile, and edit profile
- Phase 6: chat, notifications, and persisted settings
- Phase 7: open-source metadata, documentation, CI, and polish

## Near-Term Improvements

- Add real screenshot assets to `docs/SCREENSHOTS.md`.
- Export Room schemas and add migration tests for versions 1 through 4.
- Add Turbine for precise Flow emission assertions.
- Add Compose navigation tests for major user journeys.
- Add accessibility content descriptions, semantics checks, and larger-font testing.
- Make the bottom-bar notification badge observe unread Room state instead of using a static count.

## Data And Sync Improvements

- Add WorkManager for periodic fake notification refresh.
- Add explicit sync metadata and conflict rules for posts/comments.
- Replace the simulated cache-clear timestamp with a cache manager that clears selected Room tables.
- Add Paging 3 and a RemoteMediator-style boundary if the feed becomes larger.

## Production Backend Path

- Add Retrofit/OkHttp implementations behind the existing API interfaces.
- Move fake implementations into a demo build variant.
- Add secure token refresh and encrypted credential storage.
- Define API error models and retry policy.
- Add real-time chat through WebSocket or a managed messaging service.
- Add push notifications through Firebase Cloud Messaging.

## Project Scale Improvements

- Split stable boundaries into `core:*` and `feature:*` Gradle modules.
- Add convention plugins for shared Android/Kotlin configuration.
- Add baseline profiles and macrobenchmarks.
- Add dependency update automation and stricter static analysis.
- Add release signing and a tagged release workflow.

## Non-Goals For The Current Demo

- Real authentication or production account security
- Production messaging infrastructure
- Payment, advertising, or analytics SDKs
- A real social graph backend

These are intentionally excluded so the repository stays focused on Android architecture and testable UI/data patterns.
