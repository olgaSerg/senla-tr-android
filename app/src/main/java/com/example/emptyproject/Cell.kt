package com.example.emptyproject

import android.util.Log

class Cell {
    init {
        Log.v("class:", "Cell")
    }
    private var row: Int? = null
        get() = field

    private var column: Int? = null
        get()  = field

    private var isOpen: Boolean = true
        get()  = field

    private var hasFlag: Boolean = true
        get()  = field

    fun hasMine() {}
    fun open() {}
    fun addFlag() {}
    fun removeFlag() {}
    fun toggleFlag() {}
}