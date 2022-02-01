package com.example.emptyproject

import android.util.Log
import java.lang.reflect.Field

class Game {

    init {
        Log.v("class:", "Game")
    }
    private val field: Field
        get() {
            TODO()
        }
    private fun createField() {}
    private fun win() {}
    private fun gameOver() {}
    fun onLeftClick(x: Int, y:Int) {}
    fun onRightClick(x: Int, y:Int) {}
}