package com.asepssp.soccerzonecompose.ui.screen.homepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asepssp.soccerzonecompose.data.PlayerRepository
import com.asepssp.soccerzonecompose.model.PlayerItem
import com.asepssp.soccerzonecompose.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: PlayerRepository) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState<Map<Char, List<PlayerItem>>>> =
        MutableStateFlow(UiState.Loading)

    val uiState: StateFlow<UiState<Map<Char, List<PlayerItem>>>> get() = _uiState

    private val _searchResult = MutableStateFlow<List<PlayerItem>>(emptyList())
    val searchResult: StateFlow<List<PlayerItem>> get() = _searchResult

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> get() = _query

    fun getAllPlayers() {
        viewModelScope.launch {
            repository.getSortedAndGroupedAnime()
                .catch {
                    _uiState.value = UiState.Error(it.message.toString())
                }
                .collect { groupedPlayerItems ->
                    _uiState.value = UiState.Success(groupedPlayerItems)
                }
        }
    }

    fun searchPlayers() {
        val currentQuery = _query.value
        viewModelScope.launch {
            repository.searchPlayers(currentQuery)
                .catch {
                    _uiState.value = UiState.Error(it.message.toString())
                }
                .collect { searchResult ->
                    _searchResult.value = searchResult
                }
        }
    }

    fun setQuery(newQuery: String) {
        _query.value = newQuery
    }
}