package com.example.emptyproject

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

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
        val textView = textView ?: return

        textView.text = intent.getStringExtra(RESULT)

        setToolbar()
    }

    private fun setToolbar() {
        toolbar = findViewById(R.id.toolbar_result_activity)
        val toolbar = toolbar ?: return

        toolbar.title = getString(R.string.result_title)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
