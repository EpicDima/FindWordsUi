package com.epicdima.findwords.solve

import com.epicdima.findwords.common.FindWordsSolutionParameters
import com.epicdima.findwords.common.FindWordsSolutionResult
import com.epicdima.findwords.mask.MaskType
import com.epicdima.findwords.solve.FindWordsSolveUiState.Companion.NO_FULL_MATCHES
import com.epicdima.findwords.solver.Solver
import com.epicdima.findwords.solver.SolverType
import com.epicdima.findwords.trie.WordTrieType
import com.epicdima.findwords.utils.FindWordsBaseViewModel
import com.epicdima.findwords.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FindWordsSolveViewModel(
    private val parameters: FindWordsSolutionParameters,
    private val openSolutionScreen: (FindWordsSolutionResult) -> Unit
) : FindWordsBaseViewModel() {

    private val _uiStateFlow: MutableStateFlow<FindWordsSolveUiState> = MutableStateFlow(
        FindWordsSolveUiState(
            wordsFound = 0,
            fullMatchesFound = if (parameters.fullMatch) 0 else NO_FULL_MATCHES,
        )
    )
    val uiStateFlow: StateFlow<FindWordsSolveUiState>
        get() = _uiStateFlow.asStateFlow()

    init {
        scope.launch(Dispatchers.Default) {
            val solver = createSolver()
            solver.solve(
                parameters.lettersGrid.joinToString(LINES_SEPARATOR) { it.joinToString("") },
                parameters.minWordLength,
                parameters.maxWordLength,
                parameters.fullMatch
            )
            val solutionWords = solver.words
            val solutionFullMatches = solver.fullMatches
            updateUiState(solutionWords.size, solutionFullMatches.size)
            delay(1500L)
            withContext(Dispatchers.Main) {
                openSolutionScreen(
                    FindWordsSolutionResult(
                        lettersGrid = parameters.lettersGrid,
                        words = solutionWords,
                        fullMatches = solutionFullMatches
                    )
                )
            }
        }
    }

    private fun createSolver(): Solver {
        return SolverType.FORKJOIN.createInstance(
            LINES_SEPARATOR,
            MaskType.BITSET,
            if (parameters.dictionary.isEmpty()) {
                WordTrieType.SET.createInstance(WordTrieType::class.java.getResourceAsStream(Utils.DEFAULT_DICTIONARY))
            } else {
                WordTrieType.SET.createInstance(parameters.dictionary)
            }
        )
    }

    private fun updateUiState(wordsCount: Int, fullMatchesCount: Int) {
        _uiStateFlow.update { previous ->
            previous.copy(
                wordsFound = wordsCount,
                fullMatchesFound = if (parameters.fullMatch) fullMatchesCount else NO_FULL_MATCHES,
            )
        }
    }

    companion object {
        private const val LINES_SEPARATOR = "\n"
    }
}