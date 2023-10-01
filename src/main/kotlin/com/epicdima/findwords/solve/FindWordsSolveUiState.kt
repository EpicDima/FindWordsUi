package com.epicdima.findwords.solve

data class FindWordsSolveUiState(
    val wordsFound: Int,
    val fullMatchesFound: Int?,
    val solutionIsFinished: Boolean
) {

    init {
        assert(wordsFound >= 0)
    }
}
