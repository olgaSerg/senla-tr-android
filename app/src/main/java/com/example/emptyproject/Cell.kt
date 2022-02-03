package com.example.emptyproject

import android.util.Log

class Cell(private val row: Int, private val column: Int) {
    init {
        Log.v("class:", "Cell($row, $column)")
    }
    private var isOpen: Boolean = false
        get() = field
    private var hasFlag: Boolean = false
        get() = field
    fun hasMine() {}
    fun open() {}
    fun addFlag() {}
    fun removeFlag() {}
    fun toggleFlag() {}
}