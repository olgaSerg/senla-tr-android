package com.example.emptyproject.activities

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.emptyproject.R

class ResultActivity : AppCompatActivity() {

    private var toolbar: Toolbar? = null
    private var textView: TextView? = null

    companion object {
        const val RESULT = "result"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        textView = findViewById(R.id.text_view_result)
        toolbar = findViewById(R.id.toolbar_result_activity)

        val textView = textView ?: return
        val toolbar = toolbar ?: return
        textView.text = intent.getStringExtra(RESULT)

        setToolbar(toolbar)

    }

    private fun setToolbar(toolbar: Toolbar) {
        toolbar.title = getString(R.string.result_title)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
