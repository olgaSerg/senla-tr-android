package com.example.emptyproject.lesson03

fun main() {
    var res = 0
    for (i in 1 until 1000) {
        if (i % 3 == 0 || i % 5 == 0) {
            res += i
        }
    }
    println(res)
}
