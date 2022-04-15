package com.example.emptyproject

import android.content.Context
import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import okhttp3.Request
import okhttp3.Response
import okhttp3.OkHttpClient
import java.net.SocketTimeoutException

class MainActivity : AppCompatActivity() {

    private var editTextMain: EditText? = null
    private var buttonResult: Button? = null
    private var textViewResult: TextView? = null
    private var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextMain = findViewById(R.id.edit_text_main)
        buttonResult = findViewById(R.id.button_result)
        textViewResult = findViewById(R.id.text_view_result)
        progressBar = findViewById(R.id.progress_bar)

        val buttonResult = buttonResult ?: return

        buttonResult.setOnClickListener {
            val editTextMain = editTextMain ?: return@setOnClickListener
            val parameter = Uri.encode(editTextMain.text.toString())
            val url = "${URL}$parameter"
            val resultTask = ResultTask()

            resultTask.execute(url)
            buttonResult.isEnabled = false
            hideKeyboard()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(TEXT_VIEW_RESULT, textViewResult?.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        val text = savedInstanceState.getString(TEXT_VIEW_RESULT) ?: return
        val textViewResult = textViewResult ?: return
        textViewResult.text = text
    }

    inner class ResultTask : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            val progressBar = progressBar ?: return
            progressBar.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: String?): String? {
            if (params[0] == null) return null
            val request: Request =
                Request.Builder()
                    .url(params[0]!!)
                    .build()
            val client = OkHttpClient()
            try {
                val response: Response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    return response.body?.string()
                } else {
                    response.body?.close()
                }
            } catch (e: SocketTimeoutException) {
                return getString(R.string.error_connection)
            }
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            val textViewResult = textViewResult ?: return
            val progressBar = progressBar ?: return
            val buttonResult = buttonResult ?: return

            textViewResult.text = result
            progressBar.visibility = View.GONE
            buttonResult.isEnabled = true
        }
    }

    private fun hideKeyboard() {
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
    }

    companion object {
        const val URL = "http://pub.zame-dev.org/senla-training-addition/lesson-19.php?param="
        const val TEXT_VIEW_RESULT = "resultText"
    }
}





