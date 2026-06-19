# Phase 2 Summary: Fake Auth And Session

## Phase Goal

Add a complete fake authentication flow and persist the signed-in session so app launch can choose the auth or main navigation path.

## What Was Implemented

- Auth DTOs and fake `AuthApiService`
- Login and sign-up validation
- `AuthRepository` interface and implementation
- Login, sign-up, logout, and observe-session use cases
- Preferences DataStore session storage
- Splash/session check
- Login and sign-up screens
- Navigation from auth to the main app
- Logout from drawer/profile/settings paths
- Unit tests for validation and auth/session ViewModels

## Android Concepts Demonstrated

- Preferences DataStore
- Flow-backed session observation
- ViewModel validation and immutable UI state
- Sealed one-time effects
- Root navigation graph switching
- Hilt bindings for API and repository interfaces
- Coroutine suspend functions for auth actions

## Data Flow

Login:

```text
LoginScreen button
  -> LoginViewModel.login
  -> LoginUseCase
  -> AuthRepositoryImpl.login
  -> FakeAuthApiService.login
  -> AuthDto mapped to AuthSession
  -> SessionLocalDataSource.saveSession
  -> LoginEffect.NavigateToHome
```

App startup:

```text
SplashViewModel
  -> ObserveSessionUseCase
  -> AuthRepository.observeSession
  -> DataStore Flow
  -> Authenticated or Unauthenticated state
  -> Root NavHost selects main or auth graph
```

## Important Files

- `data/auth/AuthApiService.kt`: fake remote auth contract.
- `data/auth/FakeAuthApiService.kt`: delayed hard-coded auth implementation.
- `core/datastore/SessionLocalDataSource.kt`: session preference keys and Flow mapping.
- `data/repository/AuthRepositoryImpl.kt`: coordinates auth API and session persistence.
- `feature/auth/AuthValidation.kt`: login/sign-up validation rules.
- `feature/auth/LoginViewModel.kt`: login state and effects.
- `feature/auth/SignUpViewModel.kt`: registration state and effects.
- `feature/auth/SplashViewModel.kt`: session-based startup state.
- `core/navigation/AuthNavigation.kt`: auth destinations.

## Important Classes

### `SessionLocalDataSource`

Maps DataStore preferences into an optional `AuthSession`, saves user/session fields, and removes them on logout.

### `LoginViewModel`

Validates inputs, exposes loading and field errors, calls the login use case, and emits navigation/snackbar effects.

### `SessionViewModel`

Provides a small root-level logout action without exposing DataStore to navigation code.

## Interview Notes

### Why use DataStore instead of SharedPreferences?

DataStore provides an asynchronous, Flow-based API and avoids synchronous disk reads on the main thread.

### Why is navigation a one-time effect?

Navigation should happen once after successful login; it is not durable form state. A channel effect avoids accidental replay after recomposition.

### Is the auth secure?

No. It is intentionally fake. Production auth would require a backend, secure token handling, refresh logic, and likely encrypted storage.

## Learning Checklist

- [ ] I can trace login from button to DataStore.
- [ ] I understand how splash observes session Flow.
- [ ] I can explain field errors versus a general error.
- [ ] I understand why effects are separate from `LoginUiState`.
- [ ] I can explain how logout clears the navigation back stack.

## Future Improvements

- Replace fake auth with Retrofit and real backend identity.
- Store production tokens using an appropriate secure strategy.
- Add token expiration and refresh.
- Add end-to-end navigation tests for launch/login/logout.
- Add password visibility controls and stronger accessibility coverage.
