package co.bitfuse.connecthub.domain.usecase

import co.bitfuse.connecthub.domain.repository.SearchRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveRecentSearchesUseCase @Inject constructor(
    private val searchRepository: SearchRepository,
) {
    operator fun invoke(): Flow<List<String>> = searchRepository.observeRecentSearches()
}
