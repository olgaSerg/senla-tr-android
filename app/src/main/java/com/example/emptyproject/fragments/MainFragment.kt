package com.example.emptyproject.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.emptyproject.R
import com.example.emptyproject.RegexpParser
import com.example.emptyproject.SharedPreferencesManager

class MainFragment : Fragment(R.layout.fragment_main) {

    private var buttonResult: Button? = null
    private var fragmentResultClickListener: OnFragmentClickListener? = null
    private var editText: EditText? = null
    private var regexpMode: String? = null

    companion object {

        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }

    interface OnFragmentClickListener {
        fun clickResultButton(result: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentResultClickListener = try {
            context as OnFragmentClickListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement interface OnFragmentClickListener")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editText = view.findViewById(R.id.edit_text_main_fragment)
        buttonResult = view.findViewById(R.id.button_result)

        val buttonResult = buttonResult ?: return
        val editText = editText ?: return

        regexpMode = SharedPreferencesManager(requireActivity()).loadPreferencesSelectedRadioButton()
        setButtonResultClickListener(buttonResult, editText)
    }

    private fun setButtonResultClickListener(buttonResult: Button, editText: EditText) {
        buttonResult.setOnClickListener {
            val regexpMode = regexpMode ?: return@setOnClickListener
            val newStr = editText.text.toString()
            val result = RegexpParser().transformString(newStr, regexpMode)

            val fragmentResultClickListener = fragmentResultClickListener ?: return@setOnClickListener
            fragmentResultClickListener.clickResultButton(result)

        }
    }
}