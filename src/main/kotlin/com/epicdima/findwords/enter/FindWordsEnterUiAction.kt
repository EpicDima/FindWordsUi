package com.epicdima.findwords.enter

sealed interface FindWordsEnterUiAction {

    data class ChangeInput(val text: String) : FindWordsEnterUiAction

    data object Solve : FindWordsEnterUiAction

    data object IncrementMinWordLength : FindWordsEnterUiAction
    data object DecrementMinWordLength : FindWordsEnterUiAction

    data object IncrementMaxWordLength : FindWordsEnterUiAction
    data object DecrementMaxWordLength : FindWordsEnterUiAction

    data object ChangeFullMatch : FindWordsEnterUiAction
}