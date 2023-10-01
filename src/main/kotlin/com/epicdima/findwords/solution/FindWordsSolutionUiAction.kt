package com.epicdima.findwords.solution

sealed interface FindWordsSolutionUiAction {

    data object OpenWords : FindWordsSolutionUiAction
    data object OpenFullMatches : FindWordsSolutionUiAction

    data class SelectFullMatch(val id: Int) : FindWordsSolutionUiAction

    data class ToggleWord(val id: Int) : FindWordsSolutionUiAction

    data object TryAgain : FindWordsSolutionUiAction
}