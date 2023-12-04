package com.asepssp.soccerzonecompose.ui.screen.detailpage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asepssp.soccerzonecompose.data.PlayerRepository
import com.asepssp.soccerzonecompose.model.PlayerItem
import com.asepssp.soccerzonecompose.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: PlayerRepository) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<PlayerItem>> = MutableStateFlow(UiState.Loading)

    val uiState: StateFlow<UiState<PlayerItem>> get() = _uiState

    fun getPlayerById(animeId: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = UiState.Success(repository.getAnimeItemById(animeId))
        }
    }

    fun addToFavorites(animeId: String) {
        viewModelScope.launch {
            repository.addToFavorites(animeId)
        }
    }

    fun removeFromFavorite(animeId: String) {
        viewModelScope.launch {
            repository.removeFromFavorites(animeId)
        }
    }

    fun checkFavorite(animeId: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val isFavorite = repository.isFavorite(animeId)
            onResult(isFavorite)
        }
    }
}