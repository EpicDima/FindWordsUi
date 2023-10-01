package com.epicdima.findwords.solution

import com.epicdima.findwords.base.ViewModel
import com.epicdima.findwords.common.FindWordsSolutionResult
import com.epicdima.findwords.solver.WordAndMask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class FindWordsSolutionViewModel(
    private val result: FindWordsSolutionResult,
    private val openStartScreen: () -> Unit
) : ViewModel() {

    private val _uiStateFlow: MutableStateFlow<FindWordsSolutionUiState> = MutableStateFlow(
        FindWordsSolutionUiState(
            lettersGrid = result.lettersGrid,
            lettersColorGrid = emptyList(),
            maxAvailableMode = when {
                result.fullMatches.isNotEmpty() -> FindWordsSolutionUiState.Mode.FULL_MATCHES
                result.words.isNotEmpty() -> FindWordsSolutionUiState.Mode.WORDS
                else -> FindWordsSolutionUiState.Mode.NONE
            },
            selectedMode = if (result.words.isNotEmpty()) FindWordsSolutionUiState.Mode.WORDS else FindWordsSolutionUiState.Mode.NONE,
            words = result.words.mapIndexed { index, wordAndMask ->
                FindWordsSolutionUiState.Word(
                    index,
                    wordAndMask.word,
                    Random.nextLong()
                )
            },
            selectedWords = emptyList(),
            fullMatches = result.fullMatches.mapIndexed { index, list ->
                FindWordsSolutionUiState.FullCombination(
                    index,
                    list.mapIndexed { wordIndex, wordAndMask ->
                        FindWordsSolutionUiState.Word(
                            wordIndex,
                            wordAndMask.word,
                            Random.nextLong()
                        )
                    },
                )
            },
            selectedFullMatch = null
        )
    )
    val uiStateFlow: StateFlow<FindWordsSolutionUiState>
        get() = _uiStateFlow.asStateFlow()

    fun doAction(action: FindWordsSolutionUiAction) {
        when (action) {
            FindWordsSolutionUiAction.OpenWords -> openWords()
            FindWordsSolutionUiAction.OpenFullMatches -> openFullMatches()
            is FindWordsSolutionUiAction.SelectFullMatch -> selectFullMatch(action.id)
            is FindWordsSolutionUiAction.ToggleWord -> toggleWord(action.id)
            FindWordsSolutionUiAction.TryAgain -> tryAgain()
        }
    }

    private fun openWords() {
        _uiStateFlow.value = _uiStateFlow.value.copy(
            selectedMode = FindWordsSolutionUiState.Mode.WORDS,
            selectedWords = emptyList(),
            selectedFullMatch = null,
            lettersColorGrid = emptyList()
        )
    }

    private fun openFullMatches() {
        _uiStateFlow.value = _uiStateFlow.value.copy(
            selectedMode = FindWordsSolutionUiState.Mode.FULL_MATCHES,
            selectedFullMatch = null,
            selectedWords = emptyList(),
            lettersColorGrid = emptyList()
        )
    }

    private fun selectFullMatch(id: Int) {
        scope.launch(Dispatchers.Default) {
            val previous = _uiStateFlow.value
            _uiStateFlow.value = previous.copy(
                selectedFullMatch = id,
                selectedWords = List(result.fullMatches[id].size) { it },
                lettersColorGrid = result.lettersGrid.mapIndexed { rowIndex, row ->
                    List(row.size) { colIndex ->
                        val index = result.fullMatches[id].indexOfFirst { wordAndMask ->
                            wordAndMask.mask.get(rowIndex, colIndex)
                        }
                        previous.fullMatches[id].words[index].color
                    }
                }
            )
        }
    }

    private fun toggleWord(id: Int) {
        scope.launch(Dispatchers.Default) {
            val previous = _uiStateFlow.value
            val selectedWords = previous.selectedWords.toMutableList().also {
                if (id in previous.selectedWords) {
                    it.remove(id)
                } else {
                    it.add(id)
                }
            }
            val lettersColorGrid = if (selectedWords.isEmpty()) {
                emptyList()
            } else if (previous.selectedMode == FindWordsSolutionUiState.Mode.WORDS) {
                createLetterColorGridOnWordToggle(previous, selectedWords, result.words)
            } else if (previous.selectedMode == FindWordsSolutionUiState.Mode.FULL_MATCHES && previous.selectedFullMatch != null) {
                createLetterColorGridOnWordToggle(
                    previous,
                    selectedWords,
                    result.fullMatches[previous.selectedFullMatch]
                )
            } else {
                emptyList()
            }
            _uiStateFlow.value = previous.copy(
                selectedWords = selectedWords,
                lettersColorGrid = lettersColorGrid
            )
        }
    }

    private fun createLetterColorGridOnWordToggle(
        previousUiState: FindWordsSolutionUiState,
        selectedWords: List<Int>,
        wordAndMaskList: List<WordAndMask>
    ): List<List<Long>> {
        val selectedWordAndMaskList = wordAndMaskList
            .mapIndexed { index, wordAndMask -> index to wordAndMask }
            .filter { it.first in selectedWords }
        return result.lettersGrid.mapIndexed { rowIndex, row ->
            List(row.size) { colIndex ->
                val suitable = selectedWordAndMaskList
                    .filter { it.second.mask.get(rowIndex, colIndex) }
                when {
                    suitable.isEmpty() -> 0L
                    suitable.size == 1 -> previousUiState.words[suitable.first().first].color
                    else -> 0xFFFF0000
                }
            }
        }
    }

    private fun tryAgain() {
        openStartScreen()
    }
}