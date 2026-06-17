package com.example.connecthub.domain.usecase

import com.example.connecthub.domain.repository.SearchRepository
import javax.inject.Inject

class ClearRecentSearchesUseCase @Inject constructor(
    private val searchRepository: SearchRepository,
) {
    suspend operator fun invoke() {
        searchRepository.clearRecentSearches()
    }
}
