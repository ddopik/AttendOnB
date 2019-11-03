package com.attendance735.attendonb.ui.vacation.pending

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.attendance735.attendonb.ui.home.HomeActivity
import com.attendance735.attendonb.ui.home.mainstate.viewmodel.MainStateViewModel

class PendingViewModel :ViewModel() {
    private val TAG =PedingFragment::javaClass.name

    companion object{
        private var INSTANCE: PendingViewModel? = null

        fun getInstance(activity: Fragment): PendingViewModel? {
            if (INSTANCE == null) {
                INSTANCE = ViewModelProviders.of(activity).get(PendingViewModel::class.java)
            }
            return INSTANCE
        }
    }
}