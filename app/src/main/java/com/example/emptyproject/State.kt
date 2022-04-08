package com.example.emptyproject

import android.util.Log

class State(
    var messagesBuffer: ArrayList<String> = arrayListOf(),
    var printedText: String = "",
    var isRunning: Boolean = false
) {

    @Synchronized
    fun appendMessage(message: String) {
        messagesBuffer.add(message)
    }

    private fun clearMessages() {
        messagesBuffer.clear()
    }

    @Synchronized
    fun printText() {
        if (messagesBuffer.size > 0) {
            printedText = printedText + messagesBuffer.joinToString(separator = " ") + "\n"
            clearMessages()
            Log.v("TTT1", printedText)
        }
    }
}