package com.example.emptyproject

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment


class MainScreenFragment : Fragment(R.layout.main_screen_fragment) {
    private var buttonAboutProgram: Button? = null
    private var buttonAboutAuthor: Button? = null
    private var buttonExit: Button? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonAboutProgram = view.findViewById(R.id.button_about_program)
        buttonAboutAuthor = view.findViewById(R.id.button_about_author)
        buttonExit = view.findViewById(R.id.button_exit)

        val  buttonAboutProgram =  buttonAboutProgram ?: return
        val  buttonAboutAuthor =  buttonAboutAuthor ?: return
        val  buttonExit =  buttonExit ?: return

        buttonAboutProgram.setOnClickListener {
            val dialogFragmentAboutProgram = MainScreenDialogFragment()
            val args = Bundle()
            args.putString("title", getString(R.string.about_program))
            args.putString("message", getString(R.string.about_program_message))
            dialogFragmentAboutProgram.arguments = args

            dialogFragmentAboutProgram.show(childFragmentManager, MainScreenDialogFragment.TAG)
        }

        buttonAboutAuthor.setOnClickListener {
            val dialogFragmentAboutProgram = MainScreenDialogFragment()
            val args = Bundle()
            args.putString("title", getString(R.string.about_author))
            args.putString("message", getString(R.string.about_author_message))
            dialogFragmentAboutProgram.arguments = args

            dialogFragmentAboutProgram.show(childFragmentManager, MainScreenDialogFragment.TAG)
        }

        buttonExit.setOnClickListener {
            val exitDialogFragment = ExitDialogFragment()
            val args = Bundle()
            args.putString("message", getString(R.string.exit_message))
            exitDialogFragment.arguments = args

            exitDialogFragment.show(childFragmentManager, ExitDialogFragment.TAG)
        }
    }
}