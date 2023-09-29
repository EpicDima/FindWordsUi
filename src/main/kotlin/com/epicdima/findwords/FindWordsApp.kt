package com.epicdima.findwords

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.epicdima.findwords.enter.FindWordsEnterScreen
import com.epicdima.findwords.enter.FindWordsEnterViewModel
import com.epicdima.findwords.solution.FindWordsSolutionScreen
import com.epicdima.findwords.solve.FindWordsSolveScreen
import com.epicdima.findwords.start.FindWordsStartScreen

@Composable
fun FindWordsApp(exit: () -> Unit) {
    var currentScreen: FindWordsScreen by remember { mutableStateOf(FindWordsScreen.START) }
    when (currentScreen) {
        FindWordsScreen.START -> FindWordsStartScreen(
            goBack = exit,
            openEnterScreen = {
                currentScreen = FindWordsScreen.ENTER
            }
        )
        FindWordsScreen.ENTER -> FindWordsEnterScreen(
            viewModel = FindWordsEnterViewModel(),
            goBack = {
                currentScreen = FindWordsScreen.START
            },
            openSolveScreen = {
                currentScreen = FindWordsScreen.SOLVE
            }
        )
        FindWordsScreen.SOLVE -> FindWordsSolveScreen(
            goBack = {
                currentScreen = FindWordsScreen.ENTER
            },
            openSolutionScreen = {
                currentScreen = FindWordsScreen.SOLUTION
            }
        )
        FindWordsScreen.SOLUTION -> FindWordsSolutionScreen(
            goBack = {
                currentScreen = FindWordsScreen.ENTER
            },
            openStartScreen = {
                currentScreen = FindWordsScreen.START
            }
        )
    }
}

private enum class FindWordsScreen {
    START,
    ENTER,
    SOLVE,
    SOLUTION
}
