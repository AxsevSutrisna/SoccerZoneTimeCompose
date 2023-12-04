package com.asepssp.soccerzonecompose.ui.screen.homepage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asepssp.soccerzonecompose.di.Injection
import com.asepssp.soccerzonecompose.helper.ViewModelFactory
import com.asepssp.soccerzonecompose.model.PlayerItem
import com.asepssp.soccerzonecompose.ui.common.UiState
import com.asepssp.soccerzonecompose.ui.component.PlayerListItem
import com.asepssp.soccerzonecompose.ui.component.SearchPlayer

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository())
    ),
    navigateToDetailScreen: (String) -> Unit
) {
    val searchResult by viewModel.searchResult.collectAsState(initial = emptyList())
    val query by viewModel.query.collectAsState(initial = "")

    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                viewModel.getAllPlayers()
            }

            is UiState.Success -> {
                Column {
                    SearchPlayer(
                        query = query,
                        onQueryChange = { newQuery ->
                            viewModel.setQuery(newQuery)
                            viewModel.searchPlayers()
                        },
                        modifier = Modifier.background(MaterialTheme.colorScheme.primary)
                    )
                    HomeContent(
                        groupedPlayers = if (query.isEmpty()) uiState.data else emptyMap(),
                        searchResult = searchResult,
                        modifier = modifier,
                        navigateToDetail = navigateToDetailScreen,
                    )
                }
            }

            is UiState.Error -> {}
        }
    }
}

@Composable
fun HomeContent(
    groupedPlayers: Map<Char, List<PlayerItem>>,
    searchResult: List<PlayerItem>,
    modifier: Modifier = Modifier,
    navigateToDetail: (String) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(160.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.testTag("PlayerList")
    ) {
        if (searchResult.isNotEmpty()) {
            items(searchResult, key = { it.item.id }) { data ->
                PlayerListItem(
                    playerName = data.item.playerName,
                    playerPhotoUrl = data.item.photoPlayerUrl,
                    modifier = Modifier.clickable {
                        navigateToDetail(data.item.id)
                    }
                )
            }
        } else {
            groupedPlayers.entries.forEach { (_, playerItems) ->
                items(playerItems) { data ->
                    PlayerListItem(
                        playerName = data.item.playerName,
                        playerPhotoUrl = data.item.photoPlayerUrl,
                        modifier = Modifier.clickable {
                            navigateToDetail(data.item.id)
                        }
                    )
                }
            }
        }
    }
}