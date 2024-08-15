package com.epicdima.findwords.solution

sealed interface FindWordsSolutionUiAction {

    data object ShowWords : FindWordsSolutionUiAction
    data object ShowFullMatches : FindWordsSolutionUiAction

    data class SelectFullMatch(val id: Int) : FindWordsSolutionUiAction

    data class ToggleWord(val id: Int) : FindWordsSolutionUiAction

    data object Change : FindWordsSolutionUiAction
    data object New : FindWordsSolutionUiAction
}
