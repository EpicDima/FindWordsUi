package com.epicdima.findwords.enter

import androidx.compose.runtime.Immutable

@Immutable
data class FindWordsEnterUiState(
    val input: String,
    val rows: Int,
    val cols: Int,
    val dictionary: String,
    val minWordLength: Int,
    val maxWordLength: Int,
    val fullMatch: Boolean,
    val lettersGrid: List<List<Char>>,
    val solveEnabled: Boolean,
) {

    init {
        require(rows >= 0)
        require(cols >= 0)
        require(minWordLength > 0)
        require(maxWordLength > 0)
        require(maxWordLength >= minWordLength)
        require(lettersGrid.size == rows)
        require(rows == 0 || rows > 0 && lettersGrid.first().size == cols)
    }
}
