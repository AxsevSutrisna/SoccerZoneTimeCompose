package com.asepssp.soccerzonecompose.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ButtonMoreInformation(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ButtonMoreInformationPreview() {
    MaterialTheme {
        ButtonMoreInformation(text = "") {

        }
    }
}

