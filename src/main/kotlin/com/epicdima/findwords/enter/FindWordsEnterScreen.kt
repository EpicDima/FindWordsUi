package com.epicdima.findwords.enter

import androidx.compose.foundation.background
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
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
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
fun FindWordsEnterScreen(
    viewModel: FindWordsEnterViewModel,
    goBack: () -> Unit,
    openSolveScreen: () -> Unit
) {
    val uiState by viewModel.uiStateFlow.collectAsState()
    FindWordsEnterScreen(uiState, viewModel::doAction)
}

@Composable
private fun FindWordsEnterScreen(
    uiState: FindWordsEnterUiState,
    doAction: (FindWordsEnterUiAction) -> Unit
) {
    MaterialTheme {
        Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceBetween) {
            Box(modifier = Modifier.fillMaxHeight().fillMaxWidth(0.7f)) {
                LettersGrid(uiState.rows, uiState.cols, uiState.lettersGrid, doAction)
                SolveButton(doAction)
            }
            Column(modifier = Modifier.fillMaxHeight().background(Color.Gray)) {
                Parameters(uiState, doAction)
            }
        }
    }
}

@Composable
private fun BoxScope.LettersGrid(
    rows: Int,
    cols: Int,
    letters: List<List<Char>>,
    doAction: (FindWordsEnterUiAction) -> Unit
) {
    Column(modifier = Modifier.align(Alignment.Center).wrapContentSize()) {
        for (row in 0 until rows) {
            Row(modifier = Modifier.wrapContentSize()) {
                for (col in 0 until cols) {
                    TextField(
                        value = letters[row][col].toString().ifBlank { "" },
                        onValueChange = {
                            doAction(FindWordsEnterUiAction.ChangeLetter(row, col, it))
                        },
                        maxLines = 1,
                        minLines = 1,
                        singleLine = true,
                        modifier = Modifier
                            .defaultMinSize(minWidth = 56.dp, minHeight = 56.dp)
                            .size(56.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun BoxScope.SolveButton(doAction: (FindWordsEnterUiAction) -> Unit) {
    Button(
        onClick = { doAction(FindWordsEnterUiAction.Solve) },
        modifier = Modifier.align(Alignment.BottomCenter).wrapContentSize()
    ) {
        Text(text = "Решить")
    }
}

@Composable
private fun Parameters(
    uiState: FindWordsEnterUiState,
    doAction: (FindWordsEnterUiAction) -> Unit
) {
    ParametersTitle()
    ParametersGridSize(uiState.rows, uiState.cols, doAction)
    ParametersWordLength(uiState.minWordLength, uiState.maxWordLength, doAction)
    ParametersFullMatch(uiState.fullMatch, doAction)
}

@Composable
private fun ParametersTitle() {
    Text(
        text = "Параметры",
        fontSize = 28.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(top = 16.dp, bottom = 24.dp).background(Color.DarkGray)
    )
}

@Composable
private fun ParametersGridSize(
    rows: Int,
    cols: Int,
    doAction: (FindWordsEnterUiAction) -> Unit
) {
    Text(
        text = "Размер поля",
        fontSize = 24.sp,
        textAlign = TextAlign.Start,
        modifier = Modifier.padding(bottom = 12.dp).background(Color.DarkGray)
    )
    Row(modifier = Modifier.padding(bottom = 24.dp)) {
        Text(
            text = "Ширина",
            fontSize = 24.sp
        )
        NumberPicker(
            text = cols.toString(),
            increment = { doAction(FindWordsEnterUiAction.IncrementCols) },
            decrement = { doAction(FindWordsEnterUiAction.DecrementCols) },
        )
    }
    Row(modifier = Modifier.padding(bottom = 24.dp)) {
        Text(
            text = "Высота",
            fontSize = 24.sp
        )
        NumberPicker(
            text = rows.toString(),
            increment = { doAction(FindWordsEnterUiAction.IncrementRows) },
            decrement = { doAction(FindWordsEnterUiAction.DecrementRows) }
        )
    }
}

@Composable
private fun ParametersWordLength(
    minWordLength: Int,
    maxWordLength: Int,
    doAction: (FindWordsEnterUiAction) -> Unit
) {
    Text(
        text = "Длина слова",
        fontSize = 24.sp,
        modifier = Modifier.padding(bottom = 12.dp)
    )
    Row(modifier = Modifier.padding(bottom = 24.dp)) {
        Text(
            text = "Минимальная",
            fontSize = 24.sp
        )
        NumberPicker(
            text = minWordLength.toString(),
            increment = { doAction(FindWordsEnterUiAction.IncrementMinWordLength) },
            decrement = { doAction(FindWordsEnterUiAction.DecrementMinWordLength) }
        )
    }
    Row(modifier = Modifier.padding(bottom = 24.dp)) {
        Text(
            text = "Максимальная",
            fontSize = 24.sp
        )
        NumberPicker(
            text = maxWordLength.toString(),
            increment = { doAction(FindWordsEnterUiAction.IncrementMaxWordLength) },
            decrement = { doAction(FindWordsEnterUiAction.DecrementMaxWordLength) }
        )
    }
}

@Composable
private fun ParametersFullMatch(
    fullMatch: Boolean,
    doAction: (FindWordsEnterUiAction) -> Unit
) {
    Row(modifier = Modifier.padding(bottom = 24.dp)) {
        Text(
            text = "Полное совпадение",
            fontSize = 24.sp
        )
        Checkbox(
            checked = fullMatch,
            onCheckedChange = { doAction(FindWordsEnterUiAction.ChangeFullMatch) }
        )
    }
}

@Composable
private fun NumberPicker(text: String, increment: () -> Unit, decrement: () -> Unit) {
    Button(
        onClick = decrement
    ) {
        Text(
            text = "-",
            fontSize = 28.sp
        )
    }
    Text(
        text = text,
        fontSize = 28.sp
    )
    Button(
        onClick = increment
    ) {
        Text(
            text = "+",
            fontSize = 28.sp
        )
    }
}