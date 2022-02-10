package com.example.emptyproject.lesson04_4

class SocialNet {
    val h = hashMapOf(
        "Петя" to listOf("Вася", "Оля", "Маша"),
        "Вася" to listOf("Толя", "Петя"),
        "Оля" to listOf("Петя", "Маша", "Женя"),
        "Толя" to listOf("Вася", "Максим"),
        "Маша" to listOf("Толя", "Оля"),
        "Женя" to listOf("Оля", "Паша", "Катя"),
        "Паша" to listOf("Женя", "Катя"),
        "Максим" to listOf("Толя", "Алиса"),
        "Алиса" to listOf("Максим", "Катя"),
        "Катя" to listOf("Паша", "Женя", "Алиса"),
    )
}