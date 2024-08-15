package com.epicdima.findwords

import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.epicdima.findwords.utils.runOnUiThread
import java.awt.Dimension

fun main() {
    val lifecycle = LifecycleRegistry()
    val root = runOnUiThread {
        FindWordsDefaultRootComponent(DefaultComponentContext(lifecycle = lifecycle))
    }
    application {
        val windowState = rememberWindowState()
        LifecycleController(lifecycle, windowState)
        Window(
            title = "FindWords",
            state = windowState,
            onCloseRequest = ::exitApplication,
        ) {
            with(LocalDensity.current) {
                window.minimumSize = Dimension(350.dp.toPx().toInt(), 250.dp.toPx().toInt())
            }
            FindWordsApp(root)
        }
    }
}
