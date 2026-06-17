package com.example.connecthub.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.connecthub.domain.usecase.ClearRecentSearchesUseCase
import com.example.connecthub.domain.usecase.ObserveRecentSearchesUseCase
import com.example.connecthub.domain.usecase.SaveRecentSearchUseCase
import com.example.connecthub.domain.usecase.SearchUseCase
import com.example.connecthub.domain.model.SearchResults
import com.example.connecthub.feature.feed.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase,
    private val observeRecentSearchesUseCase: ObserveRecentSearchesUseCase,
    private val saveRecentSearchUseCase: SaveRecentSearchUseCase,
    private val clearRecentSearchesUseCase: ClearRecentSearchesUseCase,
) : ViewModel() {
    private val query = MutableStateFlow("")
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        observeRecentSearches()
        observeSearchResults()
    }

    fun onQueryChange(value: String) {
        query.value = value
        _uiState.update {
            it.copy(
                query = value,
                isSearching = value.isNotBlank(),
                errorMessage = null,
            )
        }
    }

    fun selectRecentSearch(value: String) {
        onQueryChange(value)
    }

    fun clearRecentSearches() {
        viewModelScope.launch {
            clearRecentSearchesUseCase()
        }
    }

    private fun observeRecentSearches() {
        viewModelScope.launch {
            observeRecentSearchesUseCase()
                .catch { throwable ->
                    _uiState.update { it.copy(errorMessage = throwable.message ?: "Unable to read recent searches") }
                }
                .collect { searches ->
                    _uiState.update { it.copy(recentSearches = searches) }
                }
        }
    }

    private fun observeSearchResults() {
        viewModelScope.launch {
            query
                .debounce(350)
                .distinctUntilChanged()
                .flatMapLatest { rawQuery ->
                    val trimmed = rawQuery.trim()
                    if (trimmed.isBlank()) {
                        flowOf(SearchResults())
                    } else {
                        saveRecentSearchUseCase(trimmed)
                        searchUseCase(trimmed)
                    }
                }
                .catch { throwable ->
                    _uiState.update {
                        it.copy(
                            isSearching = false,
                            errorMessage = throwable.message ?: "Unable to search",
                        )
                    }
                }
                .collect { results ->
                    _uiState.update {
                        it.copy(
                            users = results.users,
                            posts = results.posts.map { post -> post.toUiModel() },
                            isSearching = false,
                        )
                    }
                }
        }
    }
}
