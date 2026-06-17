package com.example.connecthub.domain.usecase

import com.example.connecthub.domain.repository.SearchRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveRecentSearchesUseCase @Inject constructor(
    private val searchRepository: SearchRepository,
) {
    operator fun invoke(): Flow<List<String>> = searchRepository.observeRecentSearches()
}
