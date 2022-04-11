package com.example.emptyproject

import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import java.util.concurrent.Executors

const val FIRST_THREAD_SLEEP_TIME = 100L
const val SECOND_THREAD_SLEEP_TIME = 800L
const val THIRD_THREAD_SLEEP_TIME = 1000L

class MainFragment : Fragment(R.layout.fragment_main) {
    private var firstTask: MyFirstTask? = null
    private var secondTask: MySecondTask? = null
    private var fourthTask: MyFourthTask? = null
    private var buttonStart: Button? = null
    private var textViewMain: TextView? = null
    private var scrollViewMain: ScrollView? = null

    @Volatile
    private var needToStop: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonStart = view.findViewById(R.id.button_start)
        textViewMain = view.findViewById(R.id.text_view_main)
        scrollViewMain = view.findViewById(R.id.scroll_view_main) ?: return

        val buttonStart = buttonStart ?: return
        val textViewMain = textViewMain ?: return
        val scrollViewMain = scrollViewMain ?: return

        addChangeTextListener(textViewMain, scrollViewMain)

        val executor = Executors.newFixedThreadPool(5)

        buttonStart.setOnClickListener {
            firstTask = MyFirstTask()
            if (firstTask != null) {
                firstTask!!.executeOnExecutor(executor)
            }
            secondTask = MySecondTask()
            if (secondTask != null) {
                secondTask!!.executeOnExecutor(executor)
            }
            val thirdTask = MyThirdTask()
            thirdTask.executeOnExecutor(executor)
            fourthTask = MyFourthTask()
            if (fourthTask != null) {
                fourthTask!!.executeOnExecutor(executor)
            }

            App.instance?.getState()?.isRunning = true
            displayState()
            needToStop = false
        }

        retainInstance = true
    }

    override fun onStart() {
        super.onStart()
        displayState()
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

    inner class MyFirstTask : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void?): Void? {
            Log.v("msg", "1st task started")
            while (true) {
                if (needToStop) {
                    Log.v("msg", "1st task finished")
                    return null
                }
                Thread.sleep(FIRST_THREAD_SLEEP_TIME)
                if (App.instance != null) {
                    App.instance!!.getState().printText()
                }
                publishProgress()

            }
            return null
        }

        override fun onProgressUpdate(vararg values: Void?) {
            super.onProgressUpdate(*values)
            displayState()
        }
    }

    inner class MySecondTask : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void?): Void? {
            Log.v("msg", "2nd task started")

            var number = 1
            while (true) {
                if (needToStop) {
                    Log.v("msg", "2st task finished")
                    return null
                }

                if (isPrimeNumber(number)) {
                    if (App.instance != null) {
                        App.instance!!.getState().appendMessage("$number")
                    }
                    if (fourthTask != null) {
                        synchronized(fourthTask!!) {
                            (fourthTask as java.lang.Object).notify()
                        }
                    }
                    Thread.sleep(SECOND_THREAD_SLEEP_TIME)
                }
                number++
            }
        }
    }

    inner class MyThirdTask : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            Log.v("msg", "3rd task started")

            var number = 1
            while (true) {
                Thread.sleep(THIRD_THREAD_SLEEP_TIME)
                if (App.instance != null) {
                    App.instance!!.getState().appendMessage("$number")
                }
                if (number == 10) {
                    needToStop = true

                    if (fourthTask != null) {
                        synchronized(fourthTask!!) {
                            (fourthTask as java.lang.Object).notify()
                        }
                    }
                    App.instance?.getState()?.isRunning = false
                    publishProgress()

                    Log.v("msg", "3rd task finished")
                    return null
                } else {
                    number++
                }
            }
        }

        override fun onProgressUpdate(vararg values: Void?) {
            super.onProgressUpdate(*values)
            displayState()
        }
    }

    inner class MyFourthTask : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            Log.v("msg", "4th task started")

            while (true) {
                if (needToStop) {
                    Log.v("msg", "4st task finished")
                    return null
                }
                if (fourthTask != null) {
                    synchronized(fourthTask!!) {
                        (fourthTask as java.lang.Object).wait()
                    }
                }
                if (App.instance != null) {
                    App.instance!!.getState().appendMessage("Yup!")
                }
            }
        }
    }

    private fun displayState() {
        if (App.instance != null) {
            buttonStart?.isEnabled = !App.instance!!.getState().isRunning
            textViewMain?.text = App.instance!!.getState().printedText
        }
    }
}