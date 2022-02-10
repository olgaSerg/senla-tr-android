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
    println(getFriends("Петя", h))
}

fun getFriends(name: String, h: HashMap<String, List<String>>): Set<String> {
    val sn = mutableListOf<String>()
    sn.add(name)
    for (friend in h.getValue(name)) {
        sn.add(friend)
        for (each in h.getValue(friend)) {
            sn.add(each)
        }
    }
    return sn.toSet()
}


