package com.example.emptyproject

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class ExitDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(MainScreenFragment.TITLE)
            .setPositiveButton(R.string.ok) { _, _ -> requireActivity().finish() }
            .setNegativeButton(R.string.cancel) { dialogInterface, _ -> dialogInterface.cancel() }
            .create()

        val args = this.arguments
        if (args != null) {
            val message = args.getString(MainScreenFragment.MESSAGE)
            return AlertDialog.Builder(requireContext())
                .setMessage(message)
                .setPositiveButton(R.string.ok) { _, _ -> requireActivity().finish() }
                .setNegativeButton(R.string.cancel) { dialogInterface, _ -> dialogInterface.cancel() }
                .create()
        }
        return dialog
    }

    companion object {
        const val TAG = "ExitScreenDialogFragment"
    }
}

