package co.bitfuse.connecthub.domain.usecase

import co.bitfuse.connecthub.domain.repository.SearchRepository
import javax.inject.Inject

class SaveRecentSearchUseCase @Inject constructor(
    private val searchRepository: SearchRepository,
) {
    suspend operator fun invoke(query: String) {
        searchRepository.saveRecentSearch(query)
    }
}
