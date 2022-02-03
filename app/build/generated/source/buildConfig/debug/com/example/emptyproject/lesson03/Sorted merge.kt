package com.example.practisekotlin.lesson03_1

fun main() {
    val list1 = listOf(1, 7, 11, 17)
    val list2 = listOf(3, 5, 13)
    val mergedList = merge(list1, list2)
    println(mergedList)
}

fun merge(list1: List<Int>, list2: List<Int>): MutableList<Int> {
    var pos1 = 0
    var pos2 = 0
    val result = mutableListOf<Int>()
    while (pos1 < list1.size || pos2 < list2.size) {
        if (pos1 < list1.size && (pos2 == list2.size || list1[pos1] < list2[pos2])) {
            result.add(list1[pos1])
            pos1++
        } else {
            result.add(list2[pos2])
            pos2++
        }
    }

    return result
}