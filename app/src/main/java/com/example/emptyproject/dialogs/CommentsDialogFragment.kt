package com.example.emptyproject.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.emptyproject.R
import com.example.emptyproject.fragments.CommentsFragment

class CommentsDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setMessage(CommentsFragment.MESSAGE)
            .setPositiveButton(getString(R.string.ok)) { _, _ -> requireActivity().onBackPressed() }
            .create()
    }
}