package com.epicdima.findwords.enter

data class FindWordsEnterUiState(
    val rows: Int,
    val cols: Int,
    val dictionary: String,
    val minWordLength: Int,
    val maxWordLength: Int,
    val fullMatch: Boolean,
    val lettersGrid: List<List<Char>>
) {

    init {
        assert(rows > 0)
        assert(cols > 0)
        assert(minWordLength > 0)
        assert(maxWordLength > 0)
        assert(maxWordLength >= minWordLength)
        assert(lettersGrid.size == rows)
        assert(lettersGrid.first().size == cols)
    }
}
