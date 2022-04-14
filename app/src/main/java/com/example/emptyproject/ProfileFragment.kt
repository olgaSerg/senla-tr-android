package com.example.emptyproject

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.lang.ClassCastException

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var textViewEmail: TextView? = null
    private var textViewFirstName: TextView? = null
    private var textViewLastName: TextView? = null
    private var textViewBirthDate: TextView? = null
    private var textViewNotes: TextView? = null
    private var buttonLogout: Button? = null
    private var clickLogoutListener: OnSendClickLogout? = null

    companion object {

        fun newInstance(): Fragment {
            return ProfileFragment()
        }
    }

    interface OnSendClickLogout{
        fun clickLogout()
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        clickLogoutListener = try {
            activity as OnSendClickLogout
        } catch (e: ClassCastException) {
            throw ClassCastException("$activity must implement OnSendClickLogout")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textViewEmail = view.findViewById(R.id.text_view_email)
        textViewFirstName = view.findViewById(R.id.text_view_first_name)
        textViewLastName = view.findViewById(R.id.text_view_last_name)
        textViewBirthDate = view.findViewById(R.id.text_view_birth_date)
        textViewNotes = view.findViewById(R.id.text_view_notes)
        buttonLogout = view.findViewById(R.id.button_logout)

        val textViewEmail = textViewEmail ?: return
        val textViewFirstName = textViewFirstName ?: return
        val textViewLastName = textViewLastName ?: return
        val textViewBirthDate = textViewBirthDate ?: return
        val textViewNotes = textViewNotes ?: return
        val buttonLogout = buttonLogout ?: return

        val profile: Profile = arguments?.getSerializable("profile") as Profile

        textViewEmail.text = profile.email
        textViewFirstName.text = profile.firstName
        textViewLastName.text = profile.lastName
        textViewBirthDate.text = profile.birthDate
        textViewNotes.text = profile.notes

        buttonLogout.setOnClickListener {
            clickLogoutListener?.clickLogout()
        }
    }



}