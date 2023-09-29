package com.epicdima.findwords.start

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FindWordsStartScreen(
    goBack: () -> Unit,
    openEnterScreen: () -> Unit
) {
    MaterialTheme {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            FindWordsTitle()

            Button(
                onClick = openEnterScreen,
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.Center)
            ) {
                Text(text = "Ввести", fontSize = 24.sp)
            }
        }

    }
}

@Composable
fun BoxScope.FindWordsTitle() {
    Text(
        text = "FindWords",
        fontSize = 32.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .wrapContentSize()
            .padding(top = 16.dp)
            .align(Alignment.TopCenter)
    )
}
