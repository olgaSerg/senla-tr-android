package com.example.emptyproject.fragments

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.lang.ClassCastException
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import bolts.CancellationTokenSource
import bolts.Task
import com.example.emptyproject.MainActivity
import com.example.emptyproject.Profile
import com.example.emptyproject.R
import com.example.emptyproject.State
import com.example.emptyproject.providers.UpdateProfileProvider

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var textViewEmail: TextView? = null
    private var textViewFirstName: TextView? = null
    private var textViewLastName: TextView? = null
    private var textViewBirthDate: TextView? = null
    private var textViewNotes: TextView? = null
    private var buttonLogout: Button? = null
    private var clickLogoutListener: OnSendClickLogout? = null
    private var pullToRefresh: SwipeRefreshLayout? = null
    private var state: State? = null
    private var updateProfileTask: Task<Unit>? = null
    private var cancellationTokenSource: CancellationTokenSource? = null

    companion object {

        fun newInstance(state: State): Fragment {
            val args = Bundle()
            args.putSerializable(MainActivity.STATE, state)
            val profileFragment = ProfileFragment()
            profileFragment.arguments = args
            return profileFragment
        }
    }

    interface OnSendClickLogout {
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

        initializeFields(view)
        cancellationTokenSource = CancellationTokenSource()

        val buttonLogout = buttonLogout ?: return

        state = arguments?.getSerializable(MainActivity.STATE) as State

        val state = state ?: return

        if (state.profile != null) {
            displayProfile(state.profile!!)
        }

        buttonLogout.setOnClickListener {
            clickLogoutListener?.clickLogout()
        }

        setRefreshListener()
    }

    override fun onPause() {
        super.onPause()
        val cancellationTokenSource = cancellationTokenSource ?: return
        cancellationTokenSource.cancel()
    }

    private fun initializeFields(view: View) {
        textViewEmail = view.findViewById(R.id.text_view_email)
        textViewFirstName = view.findViewById(R.id.text_view_first_name)
        textViewLastName = view.findViewById(R.id.text_view_last_name)
        textViewBirthDate = view.findViewById(R.id.text_view_birth_date)
        textViewNotes = view.findViewById(R.id.text_view_notes)
        buttonLogout = view.findViewById(R.id.button_logout)
        pullToRefresh = view.findViewById(R.id.swipe_container)
    }

    private fun displayProfile(profile: Profile) {
        val textViewEmail = textViewEmail ?: return
        val textViewFirstName = textViewFirstName ?: return
        val textViewLastName = textViewLastName ?: return
        val textViewBirthDate = textViewBirthDate ?: return
        val textViewNotes = textViewNotes ?: return

        textViewEmail.text = profile.email
        textViewFirstName.text = profile.firstName
        textViewLastName.text = profile.lastName
        textViewBirthDate.text = profile.birthDate
        textViewNotes.text = profile.notes
    }

    private fun setRefreshListener() {
        val state = state ?: return
        val cancellationTokenSource = cancellationTokenSource ?: return
        val pullToRefresh = pullToRefresh ?: return
        pullToRefresh.setOnRefreshListener(OnRefreshListener {
            val updateProfileProvider = UpdateProfileProvider()
            updateProfileTask =
                updateProfileProvider.updateProfileAsync(state, cancellationTokenSource.token)
                    .onSuccess { state.profile?.let { it -> displayProfile(it) } }
            Handler().postDelayed({
                pullToRefresh.isRefreshing = false
            }, 2000)
        })
    }
}
