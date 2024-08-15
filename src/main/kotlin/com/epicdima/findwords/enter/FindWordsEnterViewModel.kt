package com.epicdima.findwords.enter

import com.epicdima.findwords.common.FindWordsSolutionParameters
import com.epicdima.findwords.utils.FindWordsBaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FindWordsEnterViewModel(
    private val openSolveScreen: (FindWordsSolutionParameters) -> Unit
) : FindWordsBaseViewModel() {

    private val _uiStateFlow: MutableStateFlow<FindWordsEnterUiState> = MutableStateFlow(
        FindWordsEnterUiState(
            input = "",
            rows = 0,
            cols = 0,
            dictionary = "",
            minWordLength = 3,
            maxWordLength = 15,
            fullMatch = true,
            lettersGrid = emptyList(),
            solveEnabled = false,
        )
    )
    val uiStateFlow: StateFlow<FindWordsEnterUiState>
        get() = _uiStateFlow.asStateFlow()

    fun doAction(uiAction: FindWordsEnterUiAction) {
        when (uiAction) {
            is FindWordsEnterUiAction.ChangeInput -> changeInput(uiAction)
            FindWordsEnterUiAction.Solve -> solve()
            FindWordsEnterUiAction.DecrementMinWordLength -> decrementMinWordLength()
            FindWordsEnterUiAction.IncrementMinWordLength -> incrementMinWordLength()
            FindWordsEnterUiAction.DecrementMaxWordLength -> decrementMaxWordLength()
            FindWordsEnterUiAction.IncrementMaxWordLength -> incrementMaxWordLength()
            FindWordsEnterUiAction.ChangeFullMatch -> changeFullMatch()
        }
    }

    private fun changeInput(changeInput: FindWordsEnterUiAction.ChangeInput) {
        val lines = changeInput.text.lines()
        val rows = lines.size
        val cols = if (lines.isEmpty()) 0 else lines.maxOf { it.length }
        val lettersGrid = createMutableLettersGrid(changeInput.text)
        _uiStateFlow.update { previous ->
            previous.copy(
                input = changeInput.text,
                rows = rows,
                cols = cols,
                lettersGrid = lettersGrid,
                solveEnabled = changeInput.text.isNotEmpty(),
            )
        }
    }

    private fun solve() {
        val uiState = uiStateFlow.value
        openSolveScreen(
            FindWordsSolutionParameters(
                dictionary = uiState.dictionary,
                minWordLength = uiState.minWordLength,
                maxWordLength = uiState.maxWordLength,
                fullMatch = uiState.fullMatch,
                lettersGrid = uiState.lettersGrid,
            )
        )
    }

    private fun decrementMinWordLength() {
        _uiStateFlow.update { previous ->
            previous.copy(
                minWordLength = maxOf(
                    MIN_WORD_LENGTH,
                    minOf(MAX_WORD_LENGTH, previous.minWordLength - 1)
                )
            )
        }
    }

    private fun incrementMinWordLength() {
        _uiStateFlow.update { previous ->
            val minWordLength = maxOf(
                MIN_WORD_LENGTH,
                minOf(MAX_WORD_LENGTH, previous.minWordLength + 1)
            )
            previous.copy(
                minWordLength = minWordLength,
                maxWordLength = maxOf(minWordLength, previous.maxWordLength)
            )
        }
    }

    private fun decrementMaxWordLength() {
        _uiStateFlow.update { previous ->
            val maxWordLength = maxOf(
                MIN_WORD_LENGTH,
                minOf(MAX_WORD_LENGTH, previous.maxWordLength - 1)
            )
            previous.copy(
                maxWordLength = maxWordLength,
                minWordLength = minOf(maxWordLength, previous.minWordLength)
            )
        }
    }

    private fun incrementMaxWordLength() {
        _uiStateFlow.update { previous ->
            previous.copy(
                maxWordLength = maxOf(
                    MIN_WORD_LENGTH,
                    minOf(MAX_WORD_LENGTH, previous.maxWordLength + 1)
                )
            )
        }
    }

    private fun changeFullMatch() {
        _uiStateFlow.update { previous ->
            previous.copy(fullMatch = !previous.fullMatch)
        }
    }

    companion object {
        private const val MIN_WORD_LENGTH = 2
        private const val MAX_WORD_LENGTH = 100
    }
}