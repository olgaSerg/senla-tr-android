package com.example.emptyproject

import android.util.Log

class Field(private val cells: Array<Array<Cell>>): Drawable {
    init {
        Log.v("class:","Field")
    }

    override fun draw() {}
    fun getNeighboringMinesCount(cell: Cell) {}
    fun openCells(cell: Cell) {}
    fun findCellByCoordinates(x: Int, y: Int) {}
    fun getMinesCount() {}
    fun getRemainingCellsCount() {}
}