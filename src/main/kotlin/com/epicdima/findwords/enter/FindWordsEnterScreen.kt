package com.epicdima.findwords.enter

import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FindWordsEnterScreen(component: FindWordsEnterComponent, modifier: Modifier = Modifier) {
    val uiState by component.findWordsEnterViewModel.uiStateFlow.collectAsState()
    FindWordsEnterScreen(uiState, component.findWordsEnterViewModel::doAction, modifier)
}

@Composable
private fun FindWordsEnterScreen(
    uiState: FindWordsEnterUiState,
    doAction: (FindWordsEnterUiAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.fillMaxSize()) {
        LettersGrid(
            uiState.rows,
            uiState.cols,
            uiState.lettersGrid,
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
                            modifier = Modifier.size(56.dp)
                                .border(1.dp, MaterialTheme.colorScheme.inverseSurface),
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
    uiState: FindWordsEnterUiState,
    doAction: (FindWordsEnterUiAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(top = 12.dp, start = 12.dp, end = 12.dp)
        ) {
            ParametersInput(uiState.input, doAction)
            ParametersWordLength(uiState.minWordLength, uiState.maxWordLength, doAction)
            ParametersFullMatch(uiState.fullMatch, doAction)
        }
        Column {
            HorizontalDivider(
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            ParametersSolveButton(
                uiState.solveEnabled,
                doAction,
                modifier = Modifier.padding(bottom = 12.dp, start = 12.dp, end = 12.dp)
            )
        }
    }
}

@Composable
private fun ParametersInput(
    input: String,
    doAction: (FindWordsEnterUiAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    BasicTextField(
        value = input,
        onValueChange = { doAction(FindWordsEnterUiAction.ChangeInput(it)) },
        minLines = 5,
        modifier = modifier
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(corner = CornerSize(8.dp))
            )
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(6.dp),
        textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
        maxLines = 12,
        keyboardOptions = KeyboardOptions(
            autoCorrect = false,
            keyboardType = KeyboardType.Text
        ),
        decorationBox = { innerTextField ->
            Box(modifier = Modifier.fillMaxSize()) {
                if (input.isEmpty()) {
                    innerTextField()
                    Text(
                        "Enter text to search for solution",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                } else {
                    innerTextField()
                }
            }
        },
    )
}

@Composable
private fun ParametersWordLength(
    minWordLength: Int,
    maxWordLength: Int,
    doAction: (FindWordsEnterUiAction) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Minimum word length", fontSize = 16.sp)
        NumberPicker(
            text = minWordLength.toString(),
            increment = { doAction(FindWordsEnterUiAction.IncrementMinWordLength) },
            decrement = { doAction(FindWordsEnterUiAction.DecrementMinWordLength) }
        )
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Maximum word length", fontSize = 16.sp)
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
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Full match", fontSize = 16.sp)
        Switch(
            checked = fullMatch,
            onCheckedChange = { doAction(FindWordsEnterUiAction.ChangeFullMatch) },
        )
    }
}

@Composable
private fun ParametersSolveButton(
    enabled: Boolean,
    doAction: (FindWordsEnterUiAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = { doAction(FindWordsEnterUiAction.Solve) },
        modifier = modifier.fillMaxWidth().height(48.dp),
        enabled = enabled,
    ) {
        Text(text = "Solve", fontSize = 18.sp, textAlign = TextAlign.Center)
    }
}

@Composable
private fun NumberPicker(
    text: String,
    increment: () -> Unit,
    decrement: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.width(128.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = decrement) {
            Icon(imageVector = Icons.Rounded.Remove, contentDescription = null)
        }
        Text(
            text = text,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1.0f),
        )
        IconButton(onClick = increment) {
            Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
        }
    }
}
