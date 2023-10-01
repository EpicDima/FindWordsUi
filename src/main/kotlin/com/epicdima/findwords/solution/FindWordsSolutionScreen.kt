package com.epicdima.findwords.solution

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FindWordsSolutionScreen(viewModel: FindWordsSolutionViewModel) {
    val uiState by viewModel.uiStateFlow.collectAsState()
    FindWordsSolutionScreen(uiState, viewModel::doAction)
}

@Composable
private fun FindWordsSolutionScreen(
    uiState: FindWordsSolutionUiState,
    doAction: (FindWordsSolutionUiAction) -> Unit
) {
    Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceBetween) {
        Box(modifier = Modifier.fillMaxHeight().fillMaxWidth(0.7f)) {
            LettersGrid(uiState.lettersGrid, uiState.lettersColorGrid)
        }
        Column(modifier = Modifier.fillMaxHeight().background(Color.Gray)) {
            Parameters(uiState, doAction)
        }
    }
}

@Composable
private fun BoxScope.LettersGrid(
    lettersGrid: List<List<Char>>,
    lettersColorGrid: List<List<Long>>
) {
    Column(modifier = Modifier.align(Alignment.Center).wrapContentSize()) {
        for (rowIndex in lettersGrid.indices) {
            Row(modifier = Modifier.wrapContentSize()) {
                for (colIndex in lettersGrid[rowIndex].indices) {
                    TextField(
                        value = lettersGrid[rowIndex][colIndex].toString().ifBlank { "" },
                        onValueChange = {},
                        enabled = false,
                        maxLines = 1,
                        minLines = 1,
                        singleLine = true,
                        modifier = Modifier
                            .defaultMinSize(minWidth = 56.dp, minHeight = 56.dp)
                            .size(56.dp)
                            .let {
                                if (lettersColorGrid.isNotEmpty()) {
                                    it.background(Color(lettersColorGrid[rowIndex][colIndex]))
                                } else {
                                    it
                                }
                            }

                    )
                }
            }
        }
    }
}

@Composable
private fun Parameters(
    uiState: FindWordsSolutionUiState,
    doAction: (FindWordsSolutionUiAction) -> Unit
) {
    ParametersTitle(uiState, doAction)
    when (uiState.selectedMode) {
        FindWordsSolutionUiState.Mode.WORDS -> ParametersWords(uiState, doAction)
        FindWordsSolutionUiState.Mode.FULL_MATCHES -> ParametersFullMatches(uiState, doAction)
        FindWordsSolutionUiState.Mode.NONE -> Unit
    }
}

@Composable
private fun ParametersTitle(
    uiState: FindWordsSolutionUiState,
    doAction: (FindWordsSolutionUiAction) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth().background(Color.LightGray)) {
        if (uiState.maxAvailableMode >= FindWordsSolutionUiState.Mode.WORDS) {
            val wordsMode = (uiState.selectedMode == FindWordsSolutionUiState.Mode.WORDS)
            Text(
                text = "Слова",
                fontSize = if (wordsMode) 28.sp else 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 24.dp)
                    .fillMaxWidth(0.5f)
                    .clickable(enabled = !wordsMode) {
                        doAction(FindWordsSolutionUiAction.OpenWords)
                    }
            )
        }
        if (uiState.maxAvailableMode >= FindWordsSolutionUiState.Mode.FULL_MATCHES) {
            val fullMatchesMode =
                (uiState.selectedMode == FindWordsSolutionUiState.Mode.FULL_MATCHES)
            Text(
                text = "Комбинации",
                fontSize = if (fullMatchesMode) 28.sp else 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 24.dp)
                    .fillMaxWidth(0.5f)
                    .clickable(enabled = !fullMatchesMode) {
                        doAction(FindWordsSolutionUiAction.OpenFullMatches)
                    }
            )
        }
    }
}

@Composable
private fun ParametersWords(
    uiState: FindWordsSolutionUiState,
    doAction: (FindWordsSolutionUiAction) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        itemsIndexed(uiState.words) { index, word ->
            val interactionSource = MutableInteractionSource()
            Text(
                text = "${index + 1}. ${word.text}",
                modifier = Modifier.fillMaxWidth()
                    .hoverable(interactionSource)
                    .clickable {
                        doAction(FindWordsSolutionUiAction.ToggleWord(word.id))
                    }
                    .let {
                        if (word.id in uiState.selectedWords) {
                            it.background(Color(word.color))
                        } else {
                            it
                        }
                    }
            )
        }
    }
}


@Composable
private fun ParametersFullMatches(
    uiState: FindWordsSolutionUiState,
    doAction: (FindWordsSolutionUiAction) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        itemsIndexed(uiState.fullMatches) { fullMatchIndex, fullMatch ->
            val fullMatchInteractionSource = MutableInteractionSource()
            Text(
                text = "${fullMatchIndex + 1}. Комбинация",
                modifier = Modifier.fillMaxSize()
                    .hoverable(fullMatchInteractionSource)
                    .let {
                        if (uiState.selectedFullMatch != fullMatch.id) {
                            it.clickable {
                                doAction(FindWordsSolutionUiAction.SelectFullMatch(fullMatch.id))
                            }
                        } else {
                            it
                        }
                    }
            )
            if (uiState.selectedFullMatch == fullMatch.id) {
                fullMatch.words.forEachIndexed { index, word ->
                    val interactionSource = MutableInteractionSource()
                    Text(
                        text = "${index + 1}. ${word.text}",
                        modifier = Modifier.fillMaxWidth()
                            .hoverable(interactionSource)
                            .clickable {
                                doAction(FindWordsSolutionUiAction.ToggleWord(word.id))
                            }
                            .let {
                                if (word.id in uiState.selectedWords) {
                                    it.background(Color(word.color))
                                } else {
                                    it
                                }
                            }
                    )
                }
            }
        }
    }
}
