package com.example.emptyproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView

const val FIRST_THREAD_SLEEP_TIME = 100L
const val SECOND_THREAD_SLEEP_TIME = 800L
const val THIRD_THREAD_SLEEP_TIME = 1000L

class MainActivity : AppCompatActivity() {

    private var buttonStart: Button? = null
    private var textViewMain: TextView? = null
    private var messagesBuffer = arrayListOf<String>()
    private var firstThread: Thread? = null
    private var secondThread: Thread? = null
    private var fourthThread: Thread? = null
    private var scrollViewMain : ScrollView? = null

    @Volatile
    private var threadToStop: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonStart = findViewById(R.id.button_start)
        textViewMain = findViewById(R.id.text_view_main)
        scrollViewMain = findViewById(R.id.scroll_view_main)

        val textViewMain = textViewMain ?: return
        val buttonStart = buttonStart ?: return
        val scrollViewMain = scrollViewMain ?: return

        addChangeTextListener(textViewMain, scrollViewMain)

        buttonStart.setOnClickListener {
            buttonStart.isEnabled = false
            threadToStop = -1
            startFirstThread()
            startSecondThread()
            startThirdThread()
            startFourthThread()
        }
    }

    private fun startFirstThread() {
        firstThread = Thread {
            while (true) {
                if (threadToStop == 1) {
                    Log.v("msg","1st thread finished")
                    return@Thread
                }

                Handler(Looper.getMainLooper()).post {
                    showText()
                    clearMessages()
                }

                Thread.sleep(FIRST_THREAD_SLEEP_TIME)
            }
        }
        if (firstThread != null) {
            firstThread!!.start()
        }
    }

    private fun startSecondThread() {
        var number = 1
        secondThread = Thread {
            while (true) {
                if (threadToStop == 2) {
                    Log.v("msg","2st thread finished")
                    return@Thread
                }

                if (isPrimeNumber(number)) {
                    appendMessage("$number")
                    if (fourthThread != null) {
                        synchronized(fourthThread!!) {
                            (fourthThread as java.lang.Object).notify()
                        }
                    }
                    Thread.sleep(SECOND_THREAD_SLEEP_TIME)
                }
                number++
            }
        }
        if (secondThread != null) {
            secondThread!!.start()
        }
    }

    private fun startThirdThread() {
        var number = 1
        val thirdThread = Thread {
            while (true) {
                Thread.sleep(THIRD_THREAD_SLEEP_TIME)
                appendMessage("$number")
                if (number == 10) {
                    threadToStop = 2
                    secondThread?.join()

                    threadToStop = 4
                    if (fourthThread != null) {
                        synchronized(fourthThread!!) {
                            (fourthThread as java.lang.Object).notify()
                        }
                        fourthThread!!.join()
                    }

                    threadToStop = 1
                    firstThread?.join()

                    Handler(Looper.getMainLooper()).post { buttonStart?.isEnabled = true }
                    return@Thread
                } else {
                    number++
                }
            }
        }
        thirdThread.start()
    }

    private fun startFourthThread() {
        fourthThread = Thread() {
            while (true) {
                if (threadToStop == 4) {
                    Log.v("msg", "4st thread finished")
                    return@Thread
                }
                if (fourthThread != null) {
                    synchronized(fourthThread!!) {
                        (fourthThread as java.lang.Object).wait()
                    }
                }
                appendMessage("Yap!")
            }
        }
        if (fourthThread != null) {
            fourthThread!!.start()
        }
    }

    @Synchronized
    fun appendMessage(message: String) {
        messagesBuffer.add(message)
    }

    @Synchronized
    fun clearMessages() {
        messagesBuffer.clear()
    }

    private fun showText() {
        val textViewMain = textViewMain ?: return
        if (messagesBuffer.size > 0) {
            val newText =
                textViewMain.text.toString() + messagesBuffer.joinToString(separator = " ") + "\n"
            textViewMain.text = newText
        }
    }

    private fun isPrimeNumber(number: Int): Boolean {
        var numberOfDivisors = 0
        for (divisor in 1..number) {
            if (number % divisor == 0) {
                numberOfDivisors++
            }
        }
        return numberOfDivisors == 2
    }

    private fun addChangeTextListener(textViewMain: TextView, scrollView: ScrollView) {
        textViewMain.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }
}
