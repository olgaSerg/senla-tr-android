package com.example.emptyproject

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class MainScreenDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val args = this.arguments
        if (args != null) {
            val title = args.getString("title")
            val message = args.getString("message")
            return AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setMessage(message)
                    .setPositiveButton(getString(R.string.ok)) { _, _ -> }
                    .create()
        }
        return dialog as Dialog
    }


    companion object {
        const val TAG = "MainScreenDialogFragment"
    }
}