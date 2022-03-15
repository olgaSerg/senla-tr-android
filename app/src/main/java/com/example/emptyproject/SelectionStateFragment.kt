package com.example.emptyproject

import android.os.Bundle
import androidx.fragment.app.Fragment


class SelectionStateFragment : Fragment() {
    var lastSelection = State.MAIN

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }
}