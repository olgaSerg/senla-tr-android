package com.example.emptyproject

import kotlin.random.Random

fun main() {
    println(maxDigit())
    println(sumFirstDigit())
    println(sumDigit())
}

fun maxDigit() : Int {
    val r = Random.nextInt(100, 999)
    val firstDigit = r / 100
    val secondDigit = r % 100 / 10
    val thirdDigit = r % 10
    val res = maxOf(firstDigit, secondDigit, thirdDigit)

    println(r)

    return res
}

fun sumFirstDigit(): Int {
    val r1 = Random.nextInt(100, 999)
    val r2 = Random.nextInt(100, 999)
    val r3 = Random.nextInt(100, 999)

    val sumFirstDigits = r1 / 100 + r2 / 100 + r3 / 100

    println(r1)
    println(r2)
    println(r3)

    return sumFirstDigits

    //    println("$r1\n$r2\n$r3")
}
fun sumDigit(): Int {
    val r = Random.nextInt(100, 999)
    val firstDigit = r / 100
    val secondDigit = r % 100 / 10
    val thirdDigit = r % 10
    val res = firstDigit + secondDigit + thirdDigit

    println(r)

    return res
}
