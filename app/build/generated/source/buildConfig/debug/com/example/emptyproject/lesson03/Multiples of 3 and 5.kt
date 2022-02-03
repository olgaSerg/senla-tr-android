package com.example.practisekotlin.lesson03_1

fun main() {
    var res = 0
    for (i in 1 until 1000) {
        if (i % 3 == 0 || i % 5 == 0) {
            res += i
        }
    }
    println(res)
}
