package com.epicdima.findwords.solution

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.epicdima.findwords.solution.FindWordsSolutionUiState.Word.Companion.ERROR_COLOR

@Composable
fun FindWordsSolutionScreen(component: FindWordsSolutionComponent, modifier: Modifier = Modifier) {
    val uiState by component.findWordsSolutionViewModel.uiStateFlow.collectAsState()
    FindWordsSolutionScreen(uiState, component.findWordsSolutionViewModel::doAction, modifier)
}

@Composable
private fun FindWordsSolutionScreen(
    uiState: FindWordsSolutionUiState,
    doAction: (FindWordsSolutionUiAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.fillMaxSize()) {
        LettersGrid(
            uiState.rows,
            uiState.cols,
            uiState.lettersGrid,
            uiState.lettersColorGrid,
            modifier = Modifier.weight(1.0f).fillMaxHeight()
        )
        VerticalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.primaryContainer)
        Parameters(
            uiState,
            doAction,
            modifier = Modifier.width(320.dp).fillMaxHeight()
        )
    }
}

@Composable
private fun LettersGrid(
    rows: Int,
    cols: Int,
    lettersGrid: List<List<Char>>,
    lettersColorGrid: List<List<Long>>,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .horizontalScroll(rememberScrollState())
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(modifier = Modifier.border(2.dp, MaterialTheme.colorScheme.inverseSurface)) {
            for (row in 0..<rows) {
                Row(modifier = Modifier.wrapContentSize()) {
                    for (col in 0..<cols) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .border(1.dp, MaterialTheme.colorScheme.inverseSurface)
                                .then(
                                    if (lettersColorGrid.isNotEmpty()) {
                                        val rawColor = lettersColorGrid[row][col]
                                        if (rawColor == ERROR_COLOR) {
                                            Modifier.background(
                                                Brush.linearGradient(listOf(Color.Red, Color.White))
                                            )
                                        } else {
                                            Modifier.background(Color(rawColor))
                                        }
                                    } else {
                                        Modifier
                                    }
                                ),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(text = lettersGrid[row][col].toString(), fontSize = 18.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Parameters(
    uiState: FindWordsSolutionUiState,
    doAction: (FindWordsSolutionUiAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1.0f)) {
            ParametersTabs(uiState, doAction)
            when (uiState.selectedMode) {
                FindWordsSolutionUiState.Mode.WORDS ->
                    ParametersWords(uiState, doAction)

                FindWordsSolutionUiState.Mode.FULL_MATCHES ->
                    ParametersFullMatches(uiState, doAction)

                FindWordsSolutionUiState.Mode.NONE -> Unit
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            HorizontalDivider(
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Button(
                onClick = { doAction(FindWordsSolutionUiAction.Change) },
                modifier = Modifier.padding(horizontal = 12.dp).fillMaxWidth().height(48.dp),
                colors = ButtonDefaults.buttonColors()
                    .copy(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
            ) {
                Text("Change")
            }
            Button(
                onClick = { doAction(FindWordsSolutionUiAction.New) },
                modifier = Modifier.padding(horizontal = 12.dp).fillMaxWidth()
                    .padding(bottom = 12.dp).height(48.dp)
            ) {
                Text("New")
            }
        }
    }
}

@Composable
private fun ParametersTabs(
    uiState: FindWordsSolutionUiState,
    doAction: (FindWordsSolutionUiAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedTabIndex = when (uiState.selectedMode) {
        FindWordsSolutionUiState.Mode.WORDS -> 0
        FindWordsSolutionUiState.Mode.FULL_MATCHES -> 1
        else -> 0
    }
    TabRow(selectedTabIndex = selectedTabIndex, modifier = modifier.fillMaxWidth()) {
        val wordsMode = (uiState.selectedMode == FindWordsSolutionUiState.Mode.WORDS)
        Tab(
            selected = wordsMode,
            onClick = { doAction(FindWordsSolutionUiAction.ShowWords) },
        ) {
            Text(
                text = "Words",
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 8.dp),
            )
        }
        val fullMatchesMode = (uiState.selectedMode == FindWordsSolutionUiState.Mode.FULL_MATCHES)
        Tab(
            selected = fullMatchesMode,
            onClick = { doAction(FindWordsSolutionUiAction.ShowFullMatches) },
        ) {
            Text(
                text = "Full matches",
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 8.dp),
            )
        }
    }
}

@Composable
private fun ParametersWords(
    uiState: FindWordsSolutionUiState,
    doAction: (FindWordsSolutionUiAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier.padding(horizontal = 12.dp).fillMaxWidth()) {
        itemsIndexed(uiState.words) { index, word ->
            ParametersWord(
                index = index,
                word = word,
                doAction = doAction,
                selected = word.id in uiState.selectedWords,
            )
        }
    }
}

@Composable
private fun ParametersFullMatches(
    uiState: FindWordsSolutionUiState,
    doAction: (FindWordsSolutionUiAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier.padding(horizontal = 12.dp).fillMaxWidth()) {
        itemsIndexed(uiState.fullMatches) { fullMatchIndex, fullMatch ->
            Text(
                text = "${fullMatchIndex + 1}. Full match",
                fontSize = 15.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp)
                    .hoverable(remember { MutableInteractionSource() })
                    .clickable { doAction(FindWordsSolutionUiAction.SelectFullMatch(fullMatch.id)) }
            )
            if (uiState.selectedFullMatch == fullMatch.id) {
                Column(
                    modifier = Modifier
                        .padding(top = 2.dp, bottom = 8.dp, start = 4.dp, end = 2.dp)
                        .fillMaxWidth()
                ) {
                    fullMatch.words.forEachIndexed { index, word ->
                        ParametersWord(
                            index = index,
                            word = word,
                            doAction = doAction,
                            selected = word.id in uiState.selectedWords,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ParametersWord(
    index: Int,
    word: FindWordsSolutionUiState.Word,
    doAction: (FindWordsSolutionUiAction) -> Unit,
    selected: Boolean,
    modifier: Modifier = Modifier,
) {
    Text(
        text = "${index + 1}. ${word.text}",
        fontSize = 15.sp,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .hoverable(remember { MutableInteractionSource() })
            .clickable { doAction(FindWordsSolutionUiAction.ToggleWord(word.id)) }
            .then(
                if (selected) {
                    Modifier.background(Color(word.color))
                } else {
                    Modifier
                }
            )
    )
}
