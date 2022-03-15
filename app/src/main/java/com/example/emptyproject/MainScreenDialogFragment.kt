package com.example.emptyproject

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class MainScreenDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var dialog = AlertDialog.Builder(requireContext())
            .setTitle(MainScreenFragment.TITLE)
            .setMessage(MainScreenFragment.MESSAGE)
            .setPositiveButton(getString(R.string.ok)) { _, _ -> }
            .create()

        val args = this.arguments
        if (args != null) {
            val title = args.getString(MainScreenFragment.TITLE)
            val message = args.getString(MainScreenFragment.MESSAGE)
            dialog = AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(getString(R.string.ok)) { _, _ -> }
                .create()
        }
        return dialog
    }

    companion object {
        const val TAG = "MainScreenDialogFragment"

        fun newInstance(title: String, message: String): MainScreenDialogFragment {
            val dialogFragment = MainScreenDialogFragment()
            val args = Bundle()
            args.putString(MainScreenFragment.TITLE, title)
            args.putString(MainScreenFragment.MESSAGE, message)
            dialogFragment.arguments = args
            return dialogFragment
        }
    }
}