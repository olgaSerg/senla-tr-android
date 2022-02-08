package com.example.emptyproject

import java.util.*
import kotlin.collections.ArrayList

fun main() {
    val myList= arrayListOf<String>()
    myList.add("ГРОДНО")
    myList.add("минск")
    myList.add("вИТЕБСК")
    myList.add("брест")
    myList.add("грОДНО")
    myList.add("ГоМель")
    myList.add("могилев")
    myList.add("бРест")
    myList.add("гродно")
    myList.add("минск")
    myList.add("ВИТЕБСК")
    myList.add("брест")
    myList.add("гродно")
    myList.add("ГОМель")
    myList.add("могиЛЕВ")
    myList.add("гродно")
    myList.add("минск")
    myList.add("витебск")
    myList.add("БРест")
    myList.add("гродно")
    myList.add("гОМЕль")

    findN(myList)
    println(changeList(myList))
    println(convertListToString(myList))

    val newList = changeList(myList)
    println(findUniqueWithCount(newList as ArrayList<String>))
    println(bubbleSort(newList))
}

fun findN(list: ArrayList<String>) {
    val str = Scanner(System.`in`)
    val num = str.nextInt()
    for (i in 0 until list.size) {
        if ((i + 1) % num == 0) {
            println(list[i])
        }
    }
}

fun changeList(list: ArrayList<String>): List<String> {
    return list.map { capitalize(it) }
}

fun convertListToString(list: ArrayList<String>): String {
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
fun findUniqueWithCount(list: ArrayList<String>): Map<String, Int> {
    return list.groupingBy { it }.eachCount()
}
fun bubbleSort(list: ArrayList<String>): List<String> {
    val uniqueMap = findUniqueWithCount(list)
    val uniqueList = uniqueMap.keys.toMutableList()
    var cnt = 1
    while(cnt != 0) {
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



