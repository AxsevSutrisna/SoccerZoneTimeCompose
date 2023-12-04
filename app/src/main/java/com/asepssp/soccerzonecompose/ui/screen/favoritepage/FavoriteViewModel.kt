package com.asepssp.soccerzonecompose.ui.screen.favoritepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asepssp.soccerzonecompose.data.PlayerRepository
import com.asepssp.soccerzonecompose.model.PlayerItem
import com.asepssp.soccerzonecompose.ui.common.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: PlayerRepository) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<List<PlayerItem>>> =
        MutableStateFlow(UiState.Loading)

    val uiState: StateFlow<UiState<List<PlayerItem>>> get() = _uiState

    val favoritePlayers: Flow<List<PlayerItem>> = repository.getFavoritePlayers()

    fun getAllFavoritePlayers() {
        viewModelScope.launch {
            repository.getFavoritePlayers()
                .catch {
                    _uiState.value = UiState.Error(it.message.toString())
                }
                .collect { favoritePlayerItems ->
                    _uiState.value = UiState.Success(favoritePlayerItems)
                }
        }
    }
}