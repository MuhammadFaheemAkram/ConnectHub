package co.bitfuse.connecthub.domain.usecase

import co.bitfuse.connecthub.domain.model.SearchResults
import co.bitfuse.connecthub.domain.repository.SearchRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class SearchUseCase @Inject constructor(
    private val searchRepository: SearchRepository,
) {
    operator fun invoke(query: String): Flow<SearchResults> = searchRepository.search(query)
}
