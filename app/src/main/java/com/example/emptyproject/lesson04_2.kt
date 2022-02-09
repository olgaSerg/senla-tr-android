package com.example.emptyproject

import java.util.*

fun main() {
    val wordsListFactory = WordsListFactory()
    val myList = wordsListFactory.createWordsList()

    findN(myList)
    println(changeList(myList))
    println(convertListToString(myList))

    val newList = changeList(myList)
    println(findUniqueWithCount(newList))
    println(bubbleSort(newList))
}

fun findN(list: List<String>) {
    val str = Scanner(System.`in`)
    val num = str.nextInt()
    for (i in list.indices) {
        if ((i + 1) % num == 0) {
            println(list[i])
        }
    }
}

fun changeList(list: List<String>): List<String> {
    return list.map { capitalize(it) }
}

fun convertListToString(list: List<String>): String {
    return list.joinToString(separator = " ")
}

fun capitalize(str: String): String {
    val newStr = buildString {
        append(str[0].uppercase())
        for (i in 1 until str.length) {
            append(str[i].lowercase())
        }
    }
    return newStr
}

fun findUniqueWithCount(list: List<String>): Map<String, Int> {
    return list.groupingBy { it }.eachCount()
}

fun bubbleSort(list: List<String>): List<String> {
    val uniqueMap = findUniqueWithCount(list)
    val uniqueList = uniqueMap.keys.toMutableList()
    var cnt = 1
    while (cnt != 0) {
        cnt = 0
        for (i in 0 until uniqueList.size - 1) {
            if (uniqueMap.getValue(uniqueList[i]) > uniqueMap.getValue(uniqueList[i + 1])) {
                val temp = uniqueList[i]
                uniqueList[i] = uniqueList[i + 1]
                uniqueList[i + 1] = temp
                cnt++
            }
        }
    }
    return uniqueList
}



