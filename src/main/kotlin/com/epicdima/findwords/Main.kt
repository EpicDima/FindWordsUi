package com.epicdima.findwords

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main(): Unit = application {
    Window(
        title = "FindWords",
        onCloseRequest = ::exitApplication
    ) {
        FindWordsApp {
            exitApplication()
        }
    }
}
