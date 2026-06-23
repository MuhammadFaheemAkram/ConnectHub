package co.bitfuse.connecthub.domain.usecase

import co.bitfuse.connecthub.domain.model.AppSettings
import co.bitfuse.connecthub.domain.repository.SettingsRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {
    operator fun invoke(): Flow<AppSettings> = settingsRepository.observeSettings()
}
