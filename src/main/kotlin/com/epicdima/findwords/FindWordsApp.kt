package com.epicdima.findwords

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.epicdima.findwords.enter.FindWordsEnterScreen
import com.epicdima.findwords.solution.FindWordsSolutionScreen
import com.epicdima.findwords.solve.FindWordsSolveScreen

@Composable
fun FindWordsApp(rootComponent: FindWordsRootComponent, modifier: Modifier = Modifier) {
    MaterialTheme {
        Surface(modifier = modifier.fillMaxSize().windowInsetsPadding(WindowInsets.systemBars)) {
            val state = rootComponent.stack.subscribeAsState()
            Children(
                stack = state.value,
                modifier = Modifier.fillMaxSize(),
                animation = stackAnimation(fade() + scale()),
            ) {
                when (val instance = it.instance) {
                    is FindWordsRootComponent.Child.EnterChild ->
                        FindWordsEnterScreen(component = instance.component)

                    is FindWordsRootComponent.Child.SolveChild ->
                        FindWordsSolveScreen(component = instance.component)

                    is FindWordsRootComponent.Child.SolutionChild ->
                        FindWordsSolutionScreen(component = instance.component)
                }
            }
        }
    }
}
