package com.asepssp.soccerzonecompose.data

import com.asepssp.soccerzonecompose.model.PlayerData
import com.asepssp.soccerzonecompose.model.PlayerItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlayerRepository {

    private val playerItem = mutableListOf<PlayerItem>()
    private val favoritePLayers = mutableListOf<String>()

    init {
        if (playerItem.isEmpty()) {
            PlayerData.player.forEach {
                playerItem.add(PlayerItem(it))
            }
        }
    }

    fun getSortedAndGroupedAnime(): Flow<Map<Char, List<PlayerItem>>> {
        return flow {
            val sortedPlayers = playerItem.sortedBy { it.item.playerName }
            val groupedPlayers = sortedPlayers.groupBy { it.item.playerName[0] }
            emit(groupedPlayers)
        }
    }

    fun getAnimeItemById(playerId: String): PlayerItem {
        return playerItem.first {
            it.item.id == playerId
        }
    }

    fun searchPlayers(query: String): Flow<List<PlayerItem>> {
        return flow {
            val filteredPlayers = playerItem.filter {
                it.item.playerName.contains(query, ignoreCase = true)
            }
            emit(filteredPlayers)
        }
    }

    fun getFavoritePlayers(): Flow<List<PlayerItem>> {
        return flow {
            val favoritePlayerItems = playerItem.filter { it.item.id in favoritePLayers }
            emit(favoritePlayerItems)
        }
    }

    fun addToFavorites(playerId: String) {
        if (!favoritePLayers.contains(playerId)) {
            favoritePLayers.add(playerId)
        }
    }

    fun removeFromFavorites(playerId: String) {
        favoritePLayers.remove(playerId)
    }

    fun isFavorite(playerId: String): Boolean {
        return favoritePLayers.contains(playerId)
    }

    companion object {
        @Volatile
        private var instance: PlayerRepository? = null

        fun getInstance(): PlayerRepository = instance ?: synchronized(this) {
            PlayerRepository().apply {
                instance = this
            }
        }
    }
}