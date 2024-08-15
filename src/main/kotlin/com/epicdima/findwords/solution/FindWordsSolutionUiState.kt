package com.epicdima.findwords.solution

import androidx.compose.runtime.Immutable
import com.epicdima.findwords.solver.WordAndMask

@Immutable
data class FindWordsSolutionUiState(
    val rows: Int,
    val cols: Int,
    val lettersGrid: List<List<Char>>,
    val lettersColorGrid: List<List<Long>>,
    val maxAvailableMode: Mode,
    val selectedMode: Mode,
    val words: List<Word>,
    val selectedWords: List<Int>,
    val fullMatches: List<FullCombination>,
    val selectedFullMatch: Int,
) {

    enum class Mode {
        NONE,
        WORDS,
        FULL_MATCHES
    }

    @Immutable
    data class Word(
        val id: Int,
        val wordAndMask: WordAndMask,
        val color: Long,
    ) {
        val text: String get() = wordAndMask.word

        companion object {
            const val ERROR_COLOR = -1L
        }
    }

    @Immutable
    data class FullCombination(
        val id: Int,
        val words: List<Word>
    )

    companion object {
        const val NO_SELECTED_FULL_MATCH = -1
    }
}
