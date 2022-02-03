package com.example.emptyproject.lesson03

fun main() {
    for (i in 1..100) {
        val res = mutableListOf<String>()
        if (i % 3 == 0) res.add("Fizz")
        if (i % 5 == 0) res.add("Buzz")
        if (res.size == 0) res.add(i.toString())
        println(res.joinToString(separator=" "))
    }
}
