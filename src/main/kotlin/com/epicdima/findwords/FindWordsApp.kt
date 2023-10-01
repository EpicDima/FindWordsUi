package com.epicdima.findwords

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.epicdima.findwords.base.ViewModel
import com.epicdima.findwords.common.FindWordsSolutionParameters
import com.epicdima.findwords.common.FindWordsSolutionResult
import com.epicdima.findwords.enter.FindWordsEnterScreen
import com.epicdima.findwords.enter.FindWordsEnterViewModel
import com.epicdima.findwords.solution.FindWordsSolutionScreen
import com.epicdima.findwords.solution.FindWordsSolutionViewModel
import com.epicdima.findwords.solve.FindWordsSolveScreen
import com.epicdima.findwords.solve.FindWordsSolveViewModel
import com.epicdima.findwords.start.FindWordsStartScreen

@Composable
fun FindWordsApp(exit: () -> Unit) {
    var currentScreen: FindWordsScreen by remember { mutableStateOf(FindWordsScreen.Start) }
    var currentViewModel: ViewModel? = null
    when (val screen = currentScreen) {
        FindWordsScreen.Start -> {
            currentViewModel = null
            FindWordsStartScreen(
                goBack = exit,
                openEnterScreen = {
                    currentScreen = FindWordsScreen.Enter
                }
            )
        }
        FindWordsScreen.Enter -> {

            FindWordsEnterScreen(
                FindWordsEnterViewModel(
                    openSolveScreen = {
                        currentViewModel?.onCleared()
                        currentScreen = FindWordsScreen.Solve(it)
                    }
                ).also {
                    currentViewModel = it
                }
            )
        }
        is FindWordsScreen.Solve -> {
            FindWordsSolveScreen(
                FindWordsSolveViewModel(
                    parameters = screen.parameters,
                    openSolutionScreen = {
                        currentViewModel?.onCleared()
                        currentScreen = FindWordsScreen.Solution(it)
                    }
                ).also {
                    currentViewModel = it
                }
            )
        }
        is FindWordsScreen.Solution -> {
            FindWordsSolutionScreen(
                FindWordsSolutionViewModel(
                    result = screen.result,
                    openStartScreen = {
                        currentViewModel?.onCleared()
                        currentScreen = FindWordsScreen.Start
                    }
                ).also {
                    currentViewModel = it
                }
            )
        }
    }
}

private sealed interface FindWordsScreen {

    data object Start : FindWordsScreen

    data object Enter : FindWordsScreen

    data class Solve(
        val parameters: FindWordsSolutionParameters
    ) : FindWordsScreen

    data class Solution(
        val result: FindWordsSolutionResult
    ) : FindWordsScreen
}
