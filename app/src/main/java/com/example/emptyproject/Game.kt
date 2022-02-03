package com.example.emptyproject

import android.util.Log

class Game {
    private val field: Field
    init {
        Log.v("class:", "Game")
        val rows = 10
        val columns = 12
        val cells = Array<Array<Cell>>(rows) { row -> Array<Cell>(columns) { column -> Cell(row, column) } }
        field = Field(cells)
    }

    private fun createField() {

    }
    private fun win() {}
    private fun gameOver() {}
    fun onLeftClick(x: Int, y: Int) {}
    fun onRightClick(x: Int, y: Int) {}
}