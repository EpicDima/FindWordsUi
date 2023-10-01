package com.epicdima.findwords.solve

import com.epicdima.findwords.base.ViewModel
import com.epicdima.findwords.common.FindWordsSolutionParameters
import com.epicdima.findwords.common.FindWordsSolutionResult
import com.epicdima.findwords.mask.MaskType
import com.epicdima.findwords.solver.Solver
import com.epicdima.findwords.solver.SolverType
import com.epicdima.findwords.trie.WordTrieType
import com.epicdima.findwords.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class FindWordsSolveViewModel(
    private val parameters: FindWordsSolutionParameters,
    private val openSolutionScreen: (FindWordsSolutionResult) -> Unit
) : ViewModel() {

    private val _uiStateFlow: MutableStateFlow<FindWordsSolveUiState> = MutableStateFlow(
        FindWordsSolveUiState(
            wordsFound = 0,
            fullMatchesFound = if (parameters.fullMatch) 0 else null,
            solutionIsFinished = false
        )
    )
    val uiStateFlow: StateFlow<FindWordsSolveUiState>
        get() = _uiStateFlow.asStateFlow()

    private var solutionIsFinished: Boolean = false
    private lateinit var solver: Solver

    init {
        scope.launch(Dispatchers.Default) {
            solver = createSolver()
            checkIntermediateResult()
            prepareParametersAndSolve()
            solutionIsFinished = true
            updateUiState()
            delay(1000L)
            openSolutionScreen(
                FindWordsSolutionResult(
                    lettersGrid = parameters.lettersGrid,
                    words = solver.words,
                    fullMatches = solver.fullMatches
                )
            )
        }
    }

    private fun createSolver(): Solver {
        return SolverType.DEFAULT.createInstance(
            LINES_SEPARATOR,
            MaskType.BITSET,
            if (parameters.dictionary.isEmpty()) {
                WordTrieType.SET.createInstance(WordTrieType::class.java.getResourceAsStream(Utils.DEFAULT_DICTIONARY))
            } else {
                WordTrieType.SET.createInstance(parameters.dictionary)
            }
        )
    }

    private fun prepareParametersAndSolve() {
        solver.solve(
            parameters.lettersGrid.joinToString(LINES_SEPARATOR) { it.joinToString("") },
            parameters.minWordLength,
            parameters.maxWordLength,
            parameters.fullMatch
        )
    }

    private fun checkIntermediateResult() {
        scope.launch(Dispatchers.Unconfined) {
            while (this.isActive && !solutionIsFinished) {
                delay(100L)
                updateUiState()
            }
        }
    }

    private fun updateUiState() {
        _uiStateFlow.value = uiStateFlow.value.copy(
            wordsFound = solver.words.size,
            fullMatchesFound = if (parameters.fullMatch) solver.fullMatches.size else null,
            solutionIsFinished = solutionIsFinished
        )
    }

    companion object {
        private const val LINES_SEPARATOR = ";"
    }
}