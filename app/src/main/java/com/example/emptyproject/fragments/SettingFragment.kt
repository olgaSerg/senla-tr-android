package com.example.emptyproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.RadioGroup
import android.widget.RadioButton
import com.example.emptyproject.R
import com.example.emptyproject.SharedPreferencesManager

class SettingFragment : Fragment(R.layout.fragment_settings) {

    private var radioGroup: RadioGroup? = null
    private var sharedPreferencesManager: SharedPreferencesManager? = null

    companion object {

        fun newInstance(): SettingFragment {
            return SettingFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        radioGroup = view.findViewById(R.id.radio_group)
        sharedPreferencesManager = SharedPreferencesManager(requireActivity())

        loadPreferences()
    }

    override fun onPause() {
        super.onPause()

        val sharedPreferencesManager = sharedPreferencesManager ?: return
        val radioGroup = radioGroup ?: return

        val selectedRadioButton = getCheckedRadioButtonTag(radioGroup) ?: return
        sharedPreferencesManager.savePreferences(selectedRadioButton)
    }

    private fun loadPreferences() {
        val sharedPreferencesManager = sharedPreferencesManager ?: return
        val radioGroup = radioGroup ?: return
        val checkedRadioButton = sharedPreferencesManager.loadPreferencesSelectedRadioButton()
        val radioButtonToSelect = radioGroup.findViewWithTag<RadioButton>(checkedRadioButton)

        radioButtonToSelect.isChecked = true
    }

    private fun getCheckedRadioButtonTag(radioGroup: RadioGroup): String? {
        val checkedRadioButtonId: Int = radioGroup.checkedRadioButtonId
        val selectedRadioButton = view?.findViewById<RadioButton>(checkedRadioButtonId) ?: return null
        return selectedRadioButton.tag.toString()
    }
}