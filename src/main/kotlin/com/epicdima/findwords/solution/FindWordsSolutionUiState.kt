package com.epicdima.findwords.solution

data class FindWordsSolutionUiState(
    val lettersGrid: List<List<Char>>,
    val lettersColorGrid: List<List<Long>>,
    val maxAvailableMode: Mode,
    val selectedMode: Mode,
    val words: List<Word>,
    val selectedWords: List<Int>,
    val fullMatches: List<FullCombination>,
    val selectedFullMatch: Int?
) {

    enum class Mode {
        NONE,
        WORDS,
        FULL_MATCHES
    }

    data class Word(
        val id: Int,
        val text: String,
        val color: Long
    )

    data class FullCombination(
        val id: Int,
        val words: List<Word>
    )
}
