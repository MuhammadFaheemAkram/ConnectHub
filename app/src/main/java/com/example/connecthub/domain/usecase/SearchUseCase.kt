package com.example.connecthub.domain.usecase

import com.example.connecthub.domain.model.SearchResults
import com.example.connecthub.domain.repository.SearchRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class SearchUseCase @Inject constructor(
    private val searchRepository: SearchRepository,
) {
    operator fun invoke(query: String): Flow<SearchResults> = searchRepository.search(query)
}
