package com.example.emptyproject.lesson04_4

fun main() {
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
    getFriends("Максим", h)
}

    fun getFriends(name: String, h: HashMap<String, List<String>>) {
        for (each in h.keys) {
            if (each == name) {
                println(h.getValue(name))
                val friends = h.getValue(name)
                for (friend in friends) {
                    println(h.getValue(friend))
                }
            }
        }
    }

