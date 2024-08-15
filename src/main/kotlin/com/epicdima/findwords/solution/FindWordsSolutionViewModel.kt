package com.epicdima.findwords.solution

import com.epicdima.findwords.common.FindWordsSolutionResult
import com.epicdima.findwords.solution.FindWordsSolutionUiState.Companion.NO_SELECTED_FULL_MATCH
import com.epicdima.findwords.solution.FindWordsSolutionUiState.Word.Companion.ERROR_COLOR
import com.epicdima.findwords.utils.FindWordsBaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class FindWordsSolutionViewModel(
    private val result: FindWordsSolutionResult,
    private val onChange: () -> Unit,
    private val onNew: () -> Unit,
) : FindWordsBaseViewModel() {

    private val _uiStateFlow: MutableStateFlow<FindWordsSolutionUiState>
    val uiStateFlow: StateFlow<FindWordsSolutionUiState>
        get() = _uiStateFlow.asStateFlow()

    init {
        val mode = when {
            result.fullMatches.isNotEmpty() -> FindWordsSolutionUiState.Mode.FULL_MATCHES
            result.words.isNotEmpty() -> FindWordsSolutionUiState.Mode.WORDS
            else -> FindWordsSolutionUiState.Mode.NONE
        }
        _uiStateFlow = MutableStateFlow(
            FindWordsSolutionUiState(
                rows = result.lettersGrid.size,
                cols = if (result.lettersGrid.isEmpty()) 0 else result.lettersGrid[0].size,
                lettersGrid = result.lettersGrid,
                lettersColorGrid = emptyList(),
                maxAvailableMode = mode,
                selectedMode = mode,
                words = emptyList(),
                selectedWords = emptyList(),
                fullMatches = emptyList(),
                selectedFullMatch = NO_SELECTED_FULL_MATCH,
            )
        )
        scope.launch(Dispatchers.Default) {
            val colorWords = result.words.map { wordAndMask -> wordAndMask to Random.nextLong() }
            val words = colorWords.mapIndexed { wordIndex, colorWord ->
                FindWordsSolutionUiState.Word(wordIndex, colorWord.first, colorWord.second)
            }
            val fullMatches = result.fullMatches.mapIndexed { index, list ->
                FindWordsSolutionUiState.FullCombination(
                    index,
                    list.mapIndexed { wordIndex, wordAndMask ->
                        FindWordsSolutionUiState.Word(
                            id = wordIndex,
                            wordAndMask = wordAndMask,
                            color = colorWords.find { it.first == wordAndMask }?.second
                                ?: Random.nextLong()
                        )
                    },
                )
            }
            _uiStateFlow.update { previous ->
                previous.copy(words = words, fullMatches = fullMatches)
            }
        }
    }

    fun doAction(action: FindWordsSolutionUiAction) {
        when (action) {
            FindWordsSolutionUiAction.ShowWords -> showWords()
            FindWordsSolutionUiAction.ShowFullMatches -> showFullMatches()
            is FindWordsSolutionUiAction.SelectFullMatch -> selectFullMatch(action.id)
            is FindWordsSolutionUiAction.ToggleWord -> toggleWord(action.id)
            FindWordsSolutionUiAction.Change -> onChange()
            FindWordsSolutionUiAction.New -> onNew()
        }
    }

    private fun showWords() {
        _uiStateFlow.update { previous ->
            previous.copy(
                selectedMode = FindWordsSolutionUiState.Mode.WORDS,
                selectedWords = emptyList(),
                selectedFullMatch = NO_SELECTED_FULL_MATCH,
                lettersColorGrid = emptyList()
            )
        }
    }

    private fun showFullMatches() {
        _uiStateFlow.update { previous ->
            previous.copy(
                selectedMode = FindWordsSolutionUiState.Mode.FULL_MATCHES,
                selectedFullMatch = NO_SELECTED_FULL_MATCH,
                selectedWords = emptyList(),
                lettersColorGrid = emptyList()
            )
        }
    }

    private fun selectFullMatch(id: Int) {
        scope.launch(Dispatchers.Default) {
            _uiStateFlow.update { previous ->
                if (previous.selectedFullMatch == id) {
                    previous.copy(
                        selectedFullMatch = NO_SELECTED_FULL_MATCH,
                        selectedWords = emptyList(),
                        lettersColorGrid = emptyList(),
                    )
                } else {
                    previous.copy(
                        selectedFullMatch = id,
                        selectedWords = List(result.fullMatches[id].size) { it },
                        lettersColorGrid = result.lettersGrid.mapIndexed { rowIndex, row ->
                            List(row.size) { colIndex ->
                                val index = result.fullMatches[id].indexOfFirst { wordAndMask ->
                                    wordAndMask.mask.get(rowIndex, colIndex)
                                }
                                if (index >= 0) {
                                    previous.fullMatches[id].words[index].color
                                } else {
                                    0x00000000L
                                }
                            }
                        }
                    )
                }
            }
        }
    }

    private fun toggleWord(id: Int) {
        scope.launch(Dispatchers.Default) {
            _uiStateFlow.update { previous ->
                val selectedWords = previous.selectedWords.toMutableList().also {
                    if (id in previous.selectedWords) {
                        it.remove(id)
                    } else {
                        it.add(id)
                    }
                }
                val lettersColorGrid = when {
                    selectedWords.isEmpty() -> emptyList()
                    previous.selectedMode == FindWordsSolutionUiState.Mode.WORDS -> {
                        createLettersColorGridOnWordToggle(previous.words, selectedWords)
                    }

                    previous.selectedMode == FindWordsSolutionUiState.Mode.FULL_MATCHES
                            && previous.selectedFullMatch != NO_SELECTED_FULL_MATCH -> {
                        createLettersColorGridOnWordToggle(
                            previous.fullMatches[previous.selectedFullMatch].words,
                            selectedWords
                        )
                    }

                    else -> emptyList()
                }
                previous.copy(
                    selectedWords = selectedWords,
                    lettersColorGrid = lettersColorGrid
                )
            }
        }
    }

    private fun createLettersColorGridOnWordToggle(
        words: List<FindWordsSolutionUiState.Word>,
        selectedWords: List<Int>
    ): List<List<Long>> {
        val selectedWordAndMaskList = words.mapIndexedNotNull { wordIndex, wordAndMask ->
            if (wordIndex in selectedWords) {
                wordIndex to wordAndMask
            } else {
                null
            }
        }
        return result.lettersGrid.mapIndexed { rowIndex, row ->
            List(row.size) { colIndex ->
                val suitable = selectedWordAndMaskList
                    .filter { it.second.wordAndMask.mask.get(rowIndex, colIndex) }
                when {
                    suitable.isEmpty() -> 0L
                    suitable.size == 1 -> words[suitable.first().first].color
                    else -> ERROR_COLOR
                }
            }
        }
    }
}
