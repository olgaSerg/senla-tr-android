package com.example.emptyproject

import android.util.Log

class Field: Drawable {
    init {
        Log.v("class:","Field")
    }
     lateinit var cell: Cell

    override fun draw() {}
    fun getNeighboringMinesCount(cell: Cell) {}
    fun openCells(cell: Cell) {}
    fun findCellByCoordinates(x: Int, y: Int) {}
    fun getMinesCount() {}
    fun getRemainingCellsCount() {}
}