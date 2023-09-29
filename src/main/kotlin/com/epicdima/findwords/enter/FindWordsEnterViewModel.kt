package com.epicdima.findwords.enter

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FindWordsEnterViewModel {

    private val _uiStateFlow: MutableStateFlow<FindWordsEnterUiState> = MutableStateFlow(
        FindWordsEnterUiState(
            rows = 3,
            cols = 3,
            dictionary = "",
            minWordLength = 3,
            maxWordLength = 10,
            fullMatch = false,
            lettersGrid = createLettersGrid(rows = 3, cols = 3)
        )
    )
    val uiStateFlow: StateFlow<FindWordsEnterUiState>
        get() = _uiStateFlow.asStateFlow()

    fun doAction(uiAction: FindWordsEnterUiAction) {
        when (uiAction) {
            is FindWordsEnterUiAction.ChangeLetter -> changeLetter(uiAction)
            FindWordsEnterUiAction.Solve -> solve()
            FindWordsEnterUiAction.DecrementRows -> decrementRows()
            FindWordsEnterUiAction.IncrementRows -> incrementRows()
            FindWordsEnterUiAction.DecrementCols -> decrementCols()
            FindWordsEnterUiAction.IncrementCols -> incrementCols()
            FindWordsEnterUiAction.DecrementMinWordLength -> decrementMinWordLength()
            FindWordsEnterUiAction.IncrementMinWordLength -> incrementMinWordLength()
            FindWordsEnterUiAction.DecrementMaxWordLength -> decrementMaxWordLength()
            FindWordsEnterUiAction.IncrementMaxWordLength -> incrementMaxWordLength()
            FindWordsEnterUiAction.ChangeFullMatch -> changeFullMatch()
        }
    }

    private fun changeLetter(changeLetter: FindWordsEnterUiAction.ChangeLetter) {
        val previous = _uiStateFlow.value
        _uiStateFlow.value = previous.copy(
            lettersGrid = createLettersGridWithChange(
                previous.lettersGrid,
                changeLetter.row,
                changeLetter.col,
                if (changeLetter.letter.isEmpty() || changeLetter.letter.isBlank()) {
                    EMPTY_LETTER
                } else {
                    changeLetter.letter.first()
                }
            )
        )
    }

    private fun solve() {

    }

    private fun decrementRows() {
        val previous = _uiStateFlow.value
        val rows = if (previous.rows > MIN_ROWS) previous.rows - 1 else previous.rows
        _uiStateFlow.value = previous.copy(
            rows = rows,
            lettersGrid = createLettersGridWithCopy(rows, previous.cols, previous.lettersGrid)
        )
    }

    private fun incrementRows() {
        val previous = _uiStateFlow.value
        val rows = if (previous.rows < MAX_ROWS) previous.rows + 1 else previous.rows
        _uiStateFlow.value = previous.copy(
            rows = rows,
            lettersGrid = createLettersGridWithCopy(rows, previous.cols, previous.lettersGrid)
        )
    }

    private fun decrementCols() {
        val previous = _uiStateFlow.value
        val cols = if (previous.cols > MIN_COLS) previous.cols - 1 else previous.cols
        _uiStateFlow.value = previous.copy(
            cols = cols,
            lettersGrid = createLettersGridWithCopy(previous.rows, cols, previous.lettersGrid)
        )
    }

    private fun incrementCols() {
        val previous = _uiStateFlow.value
        val cols = if (previous.cols < MAX_COLS) previous.cols + 1 else previous.cols
        _uiStateFlow.value = previous.copy(
            cols = cols,
            lettersGrid = createLettersGridWithCopy(previous.rows, cols, previous.lettersGrid)
        )
    }

    private fun decrementMinWordLength() {
        val previous = _uiStateFlow.value
        _uiStateFlow.value = previous.copy(
            minWordLength = maxOf(
                MIN_WORD_LENGTH,
                minOf(MAX_WORD_LENGTH, previous.minWordLength - 1)
            )
        )
    }

    private fun incrementMinWordLength() {
        val previous = _uiStateFlow.value
        val minWordLength = maxOf(
            MIN_WORD_LENGTH,
            minOf(MAX_WORD_LENGTH, previous.minWordLength + 1)
        )
        _uiStateFlow.value = previous.copy(
            minWordLength = minWordLength,
            maxWordLength = maxOf(minWordLength, previous.maxWordLength)
        )
    }

    private fun decrementMaxWordLength() {
        val previous = _uiStateFlow.value
        val maxWordLength = maxOf(
            MIN_WORD_LENGTH,
            minOf(MAX_WORD_LENGTH, previous.maxWordLength - 1)
        )
        _uiStateFlow.value = previous.copy(
            maxWordLength = maxWordLength,
            minWordLength = minOf(maxWordLength, previous.minWordLength)
        )
    }

    private fun incrementMaxWordLength() {
        val previous = _uiStateFlow.value
        _uiStateFlow.value = previous.copy(
            maxWordLength = maxOf(
                MIN_WORD_LENGTH,
                minOf(MAX_WORD_LENGTH, previous.maxWordLength + 1)
            )
        )
    }

    private fun changeFullMatch() {
        val previous = _uiStateFlow.value
        _uiStateFlow.value = previous.copy(
            fullMatch = !previous.fullMatch
        )
    }

    companion object {
        private const val MIN_ROWS = 2
        private const val MAX_ROWS = 40
        private const val MIN_COLS = 2
        private const val MAX_COLS = 40
        private const val MIN_WORD_LENGTH = 2
        private const val MAX_WORD_LENGTH = 100
    }
}