package com.epicdima.findwords.solve

import androidx.compose.runtime.Immutable

@Immutable
data class FindWordsSolveUiState(
    val wordsFound: Int,
    val fullMatchesFound: Int,
) {

    init {
        require(wordsFound >= 0)
        require(fullMatchesFound == NO_FULL_MATCHES || fullMatchesFound >= 0)
    }

    companion object {
        const val NO_FULL_MATCHES = -1
    }
}
