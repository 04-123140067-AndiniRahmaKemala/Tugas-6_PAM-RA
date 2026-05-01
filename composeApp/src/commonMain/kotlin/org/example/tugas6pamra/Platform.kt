package org.example.tugas6pamra

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform