package com.example.emptyproject

import kotlin.random.Random

fun main() {
    task1maxDigit()
    task2sumFirstDigits()
    task3sumDigits()
}

fun task1maxDigit() {
    val num = Random.nextInt(100, 999)
    val d = maxDigit(num)

    println(num)
    println(d)
}

fun task2sumFirstDigits() {
    val num1 = Random.nextInt(100, 999)
    val num2 = Random.nextInt(100, 999)
    val num3 = Random.nextInt(100, 999)

    println(num1)
    println(num2)
    println(num3)
    println(sumFirstDigits(num1, num2, num3))

}
fun task3sumDigits() {
    val num = Random.nextInt(100, 999)

    println(num)
    println(sumDigits(num))
}

fun maxDigit(r: Int): Int {
    val firstDigit = r / 100
    val secondDigit = r % 100 / 10
    val thirdDigit = r % 10
    return maxOf(firstDigit, secondDigit, thirdDigit)
}

fun sumFirstDigits(r1: Int, r2: Int, r3: Int): Int {
    return r1 / 100 + r2 / 100 + r3 / 100
}

fun sumDigits(r: Int): Int {
    val firstDigit = r / 100
    val secondDigit = r % 100 / 10
    val thirdDigit = r % 10
    return firstDigit + secondDigit + thirdDigit
}
