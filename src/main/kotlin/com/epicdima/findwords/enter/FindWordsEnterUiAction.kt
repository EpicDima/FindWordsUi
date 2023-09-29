package com.epicdima.findwords.enter

sealed interface FindWordsEnterUiAction {

    data class ChangeLetter(
        val row: Int,
        val col: Int,
        val letter: String
    ) : FindWordsEnterUiAction

    data object Solve : FindWordsEnterUiAction

    data object IncrementRows : FindWordsEnterUiAction
    data object DecrementRows : FindWordsEnterUiAction

    data object IncrementCols : FindWordsEnterUiAction
    data object DecrementCols : FindWordsEnterUiAction

    data object IncrementMinWordLength : FindWordsEnterUiAction
    data object DecrementMinWordLength : FindWordsEnterUiAction

    data object IncrementMaxWordLength : FindWordsEnterUiAction
    data object DecrementMaxWordLength : FindWordsEnterUiAction

    data object ChangeFullMatch : FindWordsEnterUiAction
}