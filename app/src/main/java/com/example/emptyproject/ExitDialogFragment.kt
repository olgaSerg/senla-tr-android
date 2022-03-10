package com.example.emptyproject

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment



class ExitDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val args = this.arguments
        if (args != null) {
            val message = args.getString("message")
            return AlertDialog.Builder(requireContext())
                .setMessage(message)
                .setPositiveButton(R.string.ok) { _, _ -> requireActivity().finish() }
                .setNegativeButton(R.string.cancel) { dialogInterface, _ -> dialogInterface.cancel() }
                .create()
        }
        return dialog as Dialog
    }


    companion object {
        const val TAG = "ExitScreenDialogFragment"
    }
}

