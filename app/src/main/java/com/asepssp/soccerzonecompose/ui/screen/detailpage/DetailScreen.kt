package com.asepssp.soccerzonecompose.ui.screen.detailpage

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.asepssp.soccerzonecompose.R
import com.asepssp.soccerzonecompose.di.Injection
import com.asepssp.soccerzonecompose.helper.ViewModelFactory
import com.asepssp.soccerzonecompose.ui.common.UiState
import com.asepssp.soccerzonecompose.ui.component.ButtonMoreInformation

@Composable
fun DetailScreen(
    playerId: String,
    viewModel: DetailViewModel = viewModel(factory = ViewModelFactory(Injection.provideRepository())),
    navigateBack: () -> Unit,
) {
    val isFavorite = remember { mutableStateOf(false) }

    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                viewModel.getPlayerById(playerId)
            }

            is UiState.Success -> {
                val data = uiState.data

                viewModel.checkFavorite(playerId) { isPlayerFavorite ->
                    isFavorite.value = isPlayerFavorite
                }

                val openBrowser = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartActivityForResult()
                ) { result ->
                    when (result.resultCode) {
                        Activity.RESULT_OK -> {}
                    }
                }

                DetailContent(
                    photoPlayerUrl = data.item.photoPlayerUrl,
                    photoClubUrl = data.item.photoClubUrl,
                    playerName = data.item.playerName,
                    desc = data.item.desc,
                    club = data.item.club,
                    onBackClick = navigateBack,
                    isFavorite = isFavorite.value,
                    onToggleFavorite = {
                        if (isFavorite.value) {
                            viewModel.removeFromFavorite(playerId)
                            isFavorite.value = false
                        } else {
                            viewModel.addToFavorites(playerId)
                            isFavorite.value = true
                        }
                    },
                    onPrimaryButtonClicked = {
                        val intent =
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://www.google.com/search?q=${data.item.playerName}")
                            )
                        openBrowser.launch(intent)
                    }
                )
            }

            is UiState.Error -> {}
        }
    }
}

@Composable
fun DetailContent(
    photoPlayerUrl: String,
    photoClubUrl: String,
    playerName: String,
    desc: String,
    club: String,
    onBackClick: () -> Unit,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onPrimaryButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(1f)
        ) {
            Box {
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        tint = Color.Black,
                        contentDescription = stringResource(R.string.back),
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable { onBackClick() }
                    )
                    IconButton(
                        onClick = { onToggleFavorite() },
                        modifier = modifier.padding(top = 4.dp)
                    ) {
                        val icon =
                            if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder
                        Icon(
                            imageVector = icon,
                            tint = if (isFavorite) MaterialTheme.colorScheme.primary else Color.Black,
                            contentDescription = stringResource(R.string.add_favorite),
                        )
                    }
                }
            }
            AsyncImage(
                model = photoPlayerUrl,
                contentDescription = "playerPhoto",
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .height(350.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
            ) {
                Text(
                    text = playerName,
                    textAlign = TextAlign.Justify,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
                Text(
                    text = "Player Profile",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Left,
                    modifier = modifier
                        .padding(top = 5.dp)
                        .align(Alignment.Start)
                )
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Justify,
                    modifier = modifier.padding(top = 5.dp)
                )
                Text(
                    text = "Current Club ",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Left,
                    modifier = modifier
                        .padding(top = 16.dp, bottom = 5.dp)
                        .align(Alignment.Start)
                )
                Row(
                    modifier = modifier.align(Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AsyncImage(
                        model = photoClubUrl,
                        contentDescription = "player",
                        contentScale = ContentScale.Crop,
                        modifier = modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    )
                    Spacer(modifier = modifier.width(10.dp))
                    Column {
                        Text(
                            text = club,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = modifier
                                .padding(top = 5.dp)
                                .align(Alignment.Start)
                        )
                    }
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .padding(top = 16.dp)
                        .background(Color.LightGray)
                )
                Column(
                    modifier = modifier.padding(top = 16.dp)
                ) {
                    ButtonMoreInformation(
                        text = "Find Out More",
                        onClick = { onPrimaryButtonClicked() }
                    )
                }
            }
        }
    }
}

