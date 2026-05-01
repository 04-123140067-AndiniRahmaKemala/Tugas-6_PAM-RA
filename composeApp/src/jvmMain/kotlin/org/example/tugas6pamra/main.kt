package org.example.tugas6pamra

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Tugas 6_PAM RA",
    ) {
        App()
    }
}