# Roadmap

ConnectHub has completed the planned seven-phase portfolio build.

## Completed

- Phase 1: project foundation, app shell, design system, navigation placeholders
- Phase 2: fake auth and DataStore session
- Phase 3: feed, fake network, and offline cache
- Phase 4: post detail, comments, and create post
- Phase 5: search, bookmarks, and profile
- Phase 6: chat, notifications, and settings
- Phase 7: documentation, open-source metadata, CI, and polish

## Future Improvements

- Add screenshot assets and update `docs/SCREENSHOTS.md`.
- Add Paging 3 for the feed if the fake API grows beyond simple page loading.
- Add WorkManager for periodic fake notification sync.
- Add baseline profiles for startup and scroll performance.
- Add stricter lint or static analysis tasks.
- Add accessibility snapshots and more Compose UI coverage.
- Split into feature modules if the app grows beyond portfolio scale.
- Add a real backend adapter behind the existing repository interfaces.

## Non-Goals

- Real authentication
- Production messaging infrastructure
- Push notifications
- Payment, ads, or analytics
- A real social graph backend
