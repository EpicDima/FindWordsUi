package com.epicdima.findwords.solve

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun FindWordsSolveScreen(viewModel: FindWordsSolveViewModel) {
    val uiState by viewModel.uiStateFlow.collectAsState()
    FindWordsSolveScreen(uiState)
}

@Composable
private fun FindWordsSolveScreen(uiState: FindWordsSolveUiState) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.wrapContentSize().align(Alignment.Center)) {
            Text("Идёт решение", fontSize = 32.sp)
            Text("Найдено слов: ${uiState.wordsFound}", fontSize = 24.sp)
            if (uiState.fullMatchesFound != null) {
                Text("Найдено полных решений: ${uiState.fullMatchesFound}", fontSize = 24.sp)
            }
        }
    }
}